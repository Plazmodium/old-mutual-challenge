import { Component, inject, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CountriesService } from '../../services/countries.service';
import { CountryCardComponent } from '../../components/country-card.component';
import { ICountry, IPage } from '../../models/country.model';
import { IUiState, initialUiState } from '../../models/ui-state.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, CountryCardComponent, FormsModule],
  templateUrl: 'home.component.html'
})
export class HomeComponent {
  private countriesService = inject(CountriesService);

  public countries = signal<ICountry[]>([]);
  public uiState = signal<IUiState>(initialUiState);

  public currentPage = signal<number>(0);
  public pageSize = signal<number>(12);
  public totalPages = signal<number>(0);
  public totalElements = signal<number>(0);

  public selectedRegion = signal<string>('');
  public sortBy = signal<string>('commonName,asc');

  public regions: string[] = ['Africa', 'Americas', 'Asia', 'Europe', 'Oceania', 'Antarctic'];

  constructor() {
    effect(() => {
      this.loadCountries(
        this.currentPage(),
        this.pageSize(),
        this.sortBy(),
        this.selectedRegion()
      );
    });
  }

  private loadCountries(page: number, size: number, sort: string, region: string): void {
    this.uiState.set({ status: 'loading', message: null });

    const sortParam = sort ? [sort] : [];
    const regionParam = region || undefined;

    this.countriesService.getAllCountries(page, size, sortParam, regionParam)
      .subscribe({
        next: (pageData: IPage<ICountry>) => {
          this.countries.set(pageData.content);
          this.totalPages.set(pageData.totalPages);
          this.totalElements.set(pageData.totalElements);
          this.uiState.set({ status: 'success', message: null });
        },
        error: (err) => {
          console.error('Error fetching countries:', err);
          this.uiState.set({
            status: 'error',
            message: 'Failed to load countries. Please try again later.'
          });
        }
      });
  }

  public onPageChange(page: number): void {
    if (page >= 0 && page < this.totalPages()) {
      this.currentPage.set(page);
    }
  }

  public onRegionChange(region: string): void {
    this.selectedRegion.set(region);
    this.currentPage.set(0); // Reset to first page on filter change
  }

  public onSortChange(sort: string): void {
    this.sortBy.set(sort);
    this.currentPage.set(0); // Reset to first page on sort change
  }
}
