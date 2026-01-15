import { inject, Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { GenericHttpService } from './generic-http.service';
import { ICountry, IPage } from '../models/country.model';

@Injectable({
  providedIn: 'root'
})
export class CountriesService {
  private genericHttp = inject(GenericHttpService);
  private readonly baseUrl = 'http://localhost:8080';

  public getAllCountries(page: number = 0, size: number = 10, sort?: string[], region?: string): Observable<IPage<ICountry>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (sort && sort.length > 0) {
      sort.forEach(s => {
        params = params.append('sort', s);
      });
    }

    if (region) {
      params = params.set('region', region);
    }

    return this.genericHttp.get<IPage<ICountry>>(`${this.baseUrl}/countries`, params);
  }

  public getCountryByName(name: string): Observable<ICountry[]> {
    return this.genericHttp.get<ICountry[]>(`${this.baseUrl}/countries/${name}`);
  }
}
