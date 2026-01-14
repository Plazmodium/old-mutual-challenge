import { Routes } from '@angular/router';
import { HomeComponent } from './containers/home/home.component';
import { DetailComponent } from './containers/detail/detail.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'detail/:name', component: DetailComponent },
  { path: '**', redirectTo: '' }
];
