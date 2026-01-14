import { TestBed } from '@angular/core/testing';
import { HomeComponent } from './home.component';
import { CountriesService } from '../../services/countries.service';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';
import { ICountry } from '../../models/country.model';

describe('HomeComponent', () => {
  let countriesServiceSpy: any;
  const mockCountries: ICountry[] = [
    { commonName: 'Country 1', flagPng: 'https://example.com/1.png', flagSvg: 'https://example.com/1.svg', cca2: 'C1', cca3: 'CO1' } as ICountry,
    { commonName: 'Country 2', flagPng: 'https://example.com/2.png', flagSvg: 'https://example.com/2.svg', cca2: 'C2', cca3: 'CO2' } as ICountry
  ];

  beforeEach(async () => {
    countriesServiceSpy = {
      getAllCountries: vi.fn().mockReturnValue(of(mockCountries))
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

  it('should show loading state when no countries', () => {
    countriesServiceSpy.getAllCountries.mockReturnValue(of([]));
    const fixture = TestBed.createComponent(HomeComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Loading...');
  });
});
