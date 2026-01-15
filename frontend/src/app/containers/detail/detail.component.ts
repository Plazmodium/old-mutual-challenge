import {CommonModule, NgOptimizedImage} from '@angular/common';
import {Component, DestroyRef, inject, OnInit, signal} from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { CountriesService } from '../../services/countries.service';
import { ICountry } from '../../models/country.model';
import { IUiState, initialUiState } from '../../models/ui-state.model';
import { mapHttpError } from '../../utils/error-mapper';

@Component({
  selector: 'app-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, NgOptimizedImage],
  templateUrl: 'detail.component.html'
})
export class DetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private countriesService = inject(CountriesService);
  private destroyRef = inject(DestroyRef);

  public country = signal<ICountry | null>(null);
  public uiState = signal<IUiState>({ ...initialUiState, status: 'loading' });

  public ngOnInit(): void {
    const name = this.route.snapshot.paramMap.get('name');
    if (name) {
      this.countriesService.getCountryByName(name)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe({
          next: (data) => {
            this.country.set(data[0] || null);
            this.uiState.set({ status: 'success', message: null });
          },
          error: (err) => {
            console.error('Error fetching country detail', err);
            const { code } = mapHttpError(err);
            this.uiState.set({
              status: 'error',
              message: 'Failed to load country details. Please try again later.',
              code
            });
          }
        });
    } else {
      this.uiState.set({ status: 'idle', message: null });
    }
  }
}
