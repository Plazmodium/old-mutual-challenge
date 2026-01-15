import { TestBed } from '@angular/core/testing';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { HomeComponent } from './home.component';
import { CountriesService } from '../../services/countries.service';
import { of, throwError } from 'rxjs';
import { provideRouter } from '@angular/router';
import { ICountry, IPage } from '../../models/country.model';

describe('HomeComponent', () => {
  let countriesServiceSpy: any;
  const mockCountries: ICountry[] = [
    { commonName: 'Country 1', flagPng: 'https://example.com/1.png', flagSvg: 'https://example.com/1.svg', cca2: 'C1', cca3: 'CO1' } as ICountry,
    { commonName: 'Country 2', flagPng: 'https://example.com/2.png', flagSvg: 'https://example.com/2.svg', cca2: 'C2', cca3: 'CO2' } as ICountry
  ];

  const mockPage: IPage<ICountry> = {
    content: mockCountries,
    totalElements: 2,
    totalPages: 1,
    size: 10,
    number: 0,
    first: true,
    last: true,
    numberOfElements: 2,
    empty: false,
    pageable: { pageNumber: 0, pageSize: 10 } as any,
    sort: { sorted: true, unsorted: false, empty: false }
  };

  beforeEach(async () => {
    countriesServiceSpy = {
      getAllCountries: vi.fn().mockReturnValue(of(mockPage))
    };

    await TestBed.configureTestingModule({
      imports: [HomeComponent],
      providers: [
        { provide: CountriesService, useValue: countriesServiceSpy },
        provideRouter([])
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(HomeComponent);
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
    expect(countriesServiceSpy.getAllCountries).toHaveBeenCalled();
  });

  it('should display countries', () => {
    const fixture = TestBed.createComponent(HomeComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    const cards = compiled.querySelectorAll('app-country-card');
    expect(cards.length).toBe(2);
  });

  it('should show empty state when no countries returned', () => {
    countriesServiceSpy.getAllCountries.mockReturnValue(of({ ...mockPage, content: [], totalElements: 0, totalPages: 0, empty: true }));
    const fixture = TestBed.createComponent(HomeComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('No countries found.');
    expect(compiled.textContent).not.toContain('Loading...');
  });

  it('should show error state when service fails', () => {
    countriesServiceSpy.getAllCountries.mockReturnValue(throwError(() => new Error('API Error')));
    const fixture = TestBed.createComponent(HomeComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Failed to load countries. Please try again later.');
    expect(compiled.textContent).not.toContain('Loading...');
  });
});
