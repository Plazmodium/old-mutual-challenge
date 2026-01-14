import { Component, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';

import { CountriesService } from '../../services/countries.service';
import { CountryCardComponent } from '../../components/country-card.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, CountryCardComponent],
  templateUrl: 'home.component.html'
})
export class HomeComponent {
  private countriesService = inject(CountriesService);
  public countries = toSignal(this.countriesService.getAllCountries(), {initialValue: []});
  public isLoading = computed(() => !this.countries().length);
}
