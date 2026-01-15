import { Routes } from '@angular/router';
import { HomeComponent } from './containers/home/home.component';
import { DetailComponent } from './containers/detail/detail.component';
import { NotFoundComponent } from './containers/not-found/not-found.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'detail/:name', component: DetailComponent },
  { path: '**', component: NotFoundComponent }
];
