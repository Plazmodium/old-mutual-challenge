import { Component, input } from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import { RouterLink } from '@angular/router';

import { ICountry } from '../models/country.model';

@Component({
  selector: 'app-country-card',
  standalone: true,
  imports: [RouterLink, NgOptimizedImage],
  templateUrl: 'country-card.component.html'
})
export class CountryCardComponent {
  public country = input.required<ICountry>();
}
