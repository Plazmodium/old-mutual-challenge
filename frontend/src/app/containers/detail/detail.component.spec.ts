import { TestBed } from '@angular/core/testing';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { DetailComponent } from './detail.component';
import { CountriesService } from '../../services/countries.service';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { of, throwError, NEVER } from 'rxjs';
import { ICountry } from '../../models/country.model';

describe('DetailComponent', () => {
  let countriesServiceSpy: any;
  const mockCountry: ICountry = {
    commonName: 'Test Country',
    officialName: 'Official Test Country',
    flagPng: 'https://example.com/flag.png',
    flagSvg: 'https://example.com/flag.svg',
    capital: 'Test Capital',
    region: 'Test Region',
    subregion: 'Test Subregion',
    population: 1000,
    cca2: 'TC',
    cca3: 'TST'
  };

  beforeEach(async () => {
    countriesServiceSpy = {
      getCountryByName: vi.fn().mockReturnValue(of([mockCountry]))
    };

    await TestBed.configureTestingModule({
      imports: [DetailComponent],
      providers: [
        { provide: CountriesService, useValue: countriesServiceSpy },
        provideRouter([]),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: (key: string) => (key === 'name' ? 'Test Country' : null)
              }
            }
          }
        }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(DetailComponent);
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
    expect(countriesServiceSpy.getCountryByName).toHaveBeenCalledWith('Test Country');
  });

  it('should display country details after loading', () => {
    const fixture = TestBed.createComponent(DetailComponent);
    fixture.detectChanges();

    expect(fixture.componentInstance.uiState().status).toBe('success');
    expect(fixture.componentInstance.country()).toEqual(mockCountry);

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Official Test Country');
    expect(compiled.textContent).toContain('Common Name: Test Country');
  });

  it('should handle error when fetching country', () => {
    countriesServiceSpy.getCountryByName.mockReturnValue(throwError(() => new Error('API Error')));
    const fixture = TestBed.createComponent(DetailComponent);
    fixture.detectChanges();

    expect(fixture.componentInstance.uiState().status).toBe('error');
    expect(fixture.componentInstance.country()).toBeNull();
    expect(fixture.componentInstance.uiState().message).toBe('Failed to load country details. Please try again later.');

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Failed to load country details. Please try again later.');
  });

  it('should handle no name in route', () => {
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      imports: [DetailComponent],
      providers: [
        { provide: CountriesService, useValue: countriesServiceSpy },
        provideRouter([]),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: (key: string) => null
              }
            }
          }
        }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(DetailComponent);
    fixture.detectChanges();
    expect(fixture.componentInstance.uiState().status).toBe('idle');
    expect(countriesServiceSpy.getCountryByName).not.toHaveBeenCalled();
  });

  it('should show loading state', () => {
    countriesServiceSpy.getCountryByName.mockReturnValue(NEVER);
    const fixture = TestBed.createComponent(DetailComponent);
    fixture.detectChanges();

    expect(fixture.componentInstance.uiState().status).toBe('loading');
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Loading country details...');
  });
});
