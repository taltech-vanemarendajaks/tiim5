import { Routes } from '@angular/router';
import { Layout } from './shared/components/layout/layout';

export const routes: Routes = [
  {
    path: '',
    component: Layout,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./components/dashboard/dashboard').then((m) => m.Dashboard),
      },
      {
        path: 'studies',
        loadComponent: () =>
          import('./components/studies/studies').then((m) => m.Studies),
      },
      {
        path: 'curriculum',
        children: [
          {
            path: '',
            loadComponent: () =>
              import('./components/curriculum/curriculum').then((m) => m.Curriculum),
          },
          {
            path: 'new',
            loadComponent: () =>
              import(
                './components/curriculum/curriculum-planning/curriculum-planning'
              ).then((m) => m.CurriculumPlanning),
          },
        ],
      },
      {
        path: 'schedule',
        loadComponent: () =>
          import('./components/schedule/schedule').then((m) => m.Schedule),
      },
      {
        path: 'settings',
        loadComponent: () =>
          import('./components/settings/settings').then((m) => m.Settings),
      },
      { path: '**', redirectTo: 'dashboard' },
    ],
  },
];
