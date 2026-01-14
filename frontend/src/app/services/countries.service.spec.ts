import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { CountriesService } from './countries.service';
import { GenericHttpService } from './generic-http.service';
import { ICountry } from '../models/country.model';

describe('CountriesService', () => {
  let service: CountriesService;
  let genericHttpSpy: any;

  beforeEach(() => {
    genericHttpSpy = {
      get: vi.fn()
    };

    TestBed.configureTestingModule({
      providers: [
        CountriesService,
        { provide: GenericHttpService, useValue: genericHttpSpy }
      ]
    });
    service = TestBed.inject(CountriesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all countries', () => {
    const mockCountries: ICountry[] = [{ commonName: 'Test Country' } as any];
    genericHttpSpy.get.mockReturnValue(of(mockCountries));

    service.getAllCountries().subscribe(countries => {
      expect(countries).toEqual(mockCountries);
      expect(genericHttpSpy.get).toHaveBeenCalledWith('http://localhost:8080/countries');
    });
  });

  it('should get country by name', () => {
    const mockCountry: ICountry[] = [{ commonName: 'Test Country' } as any];
    genericHttpSpy.get.mockReturnValue(of(mockCountry));

    service.getCountryByName('Test').subscribe(country => {
      expect(country).toEqual(mockCountry);
      expect(genericHttpSpy.get).toHaveBeenCalledWith('http://localhost:8080/countries/Test');
    });
  });
});
