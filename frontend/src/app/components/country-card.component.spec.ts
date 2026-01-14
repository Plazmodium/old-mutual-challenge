import { TestBed } from '@angular/core/testing';
import { CountryCardComponent } from './country-card.component';
import { provideRouter } from '@angular/router';
import { ICountry } from '../models/country.model';

describe('CountryCardComponent', () => {
  const mockCountry: ICountry = {
    commonName: 'Test Country',
    officialName: 'The Official Test Country',
    cca2: 'TC',
    cca3: 'TST',
    flagPng: 'https://example.com/flag.png',
    flagSvg: 'https://example.com/flag.svg',
    capital: 'Test Capital',
    region: 'Test Region',
    subregion: 'Test Subregion',
    population: 1000000
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CountryCardComponent],
      providers: [provideRouter([])]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(CountryCardComponent);
    fixture.componentRef.setInput('country', mockCountry);
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render country details', () => {
    const fixture = TestBed.createComponent(CountryCardComponent);
    fixture.componentRef.setInput('country', mockCountry);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h3')?.textContent).toContain('Test Country');
    const img = compiled.querySelector('img');
    expect(img?.getAttribute('alt')).toBe('Test Country');
  });
});
