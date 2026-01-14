import {CommonModule, NgOptimizedImage} from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { CountriesService } from '../../services/countries.service';
import { ICountry } from '../../models/country.model';

@Component({
  selector: 'app-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, NgOptimizedImage],
  templateUrl: 'detail.component.html'
})
export class DetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private countriesService = inject(CountriesService);

  public country = signal<ICountry | null>(null);
  public loading = signal(true);

  public ngOnInit(): void {
    const name = this.route.snapshot.paramMap.get('name');
    if (name) {
      this.countriesService.getCountryByName(name).subscribe({
        next: (data) => {
          this.country.set(data[0] || null);
          this.loading.set(false);
        },
        error: (err) => {
          console.error('Error fetching country detail', err);
          this.loading.set(false);
        }
      });
    } else {
      this.loading.set(false);
    }
  }
}
