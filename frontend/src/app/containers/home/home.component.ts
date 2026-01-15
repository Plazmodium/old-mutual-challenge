import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs';

import { CountriesService } from '../../services/countries.service';
import { CountryCardComponent } from '../../components/country-card.component';
import { ICountry } from '../../models/country.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, CountryCardComponent],
  templateUrl: 'home.component.html'
})
export class HomeComponent {
  private countriesService = inject(CountriesService);

  public countries = signal<ICountry[]>([]);
  public isLoading = signal<boolean>(true);
  public error = signal<string | null>(null);

  constructor() {
    this.loadCountries();
  }

  private loadCountries(): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.countriesService.getAllCountries()
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: (countries) => this.countries.set(countries),
        error: (err) => {
          console.error('Error fetching countries:', err);
          this.error.set('Failed to load countries. Please try again later.');
        }
      });
  }
}
