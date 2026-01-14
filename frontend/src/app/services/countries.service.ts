import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { GenericHttpService } from './generic-http.service';
import { ICountry } from '../models/country.model';

@Injectable({
  providedIn: 'root'
})
export class CountriesService {
  private genericHttp = inject(GenericHttpService);
  private readonly baseUrl = 'http://localhost:8080';

  public getAllCountries(): Observable<ICountry[]> {
    return this.genericHttp.get<ICountry[]>(`${this.baseUrl}/countries`);
  }

  public getCountryByName(name: string): Observable<ICountry[]> {
    return this.genericHttp.get<ICountry[]>(`${this.baseUrl}/countries/${name}`);
  }
}
