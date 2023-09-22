import { Routes } from '@angular/router';
import { TabsPage } from './tabs.page';

export const routes: Routes = [
  {
    path: '',
    component: TabsPage,
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./dashboard/dashboard.page').then((m) => m.DashboardPage),
      },
      {
        path: 'configuration',
        loadComponent: () =>
          import('./configuration/configuration.page').then(
            (m) => m.ConfigurationPage
          ),
      },
      {
        path: 'public',
        loadComponent: () =>
          import('./public/public.page').then((m) => m.PublicPage),
      },
      {
        path: '',
        redirectTo: '/dashboard',
        pathMatch: 'full',
      },
    ],
  },
  {
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full',
  },
];
