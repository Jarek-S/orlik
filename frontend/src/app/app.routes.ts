import {Routes} from '@angular/router';
import {MainLayoutComponent} from './layouts/main-layout/main-layout.component';
import {HomeComponent} from './features/home/home.component';
import {DemoComponent} from './features/demo/demo.component';
import {GroupsComponent} from './features/groups/groups.component';
import {authGuard} from './core/guards/auth-guard';

export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      // Public
      {path: '', component: HomeComponent},
      {path: 'demo', component: DemoComponent},
      // Restricted
      {
        path: 'dashboard',
        canActivate: [authGuard],
        loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'groups',
        component: GroupsComponent,
        canActivate: [authGuard]
      },
    ]
  },
  {path: '**', redirectTo: ''}
];
