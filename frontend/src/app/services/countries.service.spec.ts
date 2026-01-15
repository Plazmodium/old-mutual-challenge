import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { CountriesService } from './countries.service';
import { GenericHttpService } from './generic-http.service';
import { ICountry, IPage } from '../models/country.model';
import { HttpParams } from '@angular/common/http';

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

  it('should get all countries with pagination', () => {
    const mockPage: IPage<ICountry> = { content: [{ commonName: 'Test Country' } as any] } as any;
    genericHttpSpy.get.mockReturnValue(of(mockPage));

    service.getAllCountries(0, 10).subscribe(page => {
      expect(page).toEqual(mockPage);
      expect(genericHttpSpy.get).toHaveBeenCalledWith(
        'http://localhost:8080/countries',
        expect.any(HttpParams)
      );

      const params = genericHttpSpy.get.mock.calls[0][1] as HttpParams;
      expect(params.get('page')).toBe('0');
      expect(params.get('size')).toBe('10');
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
