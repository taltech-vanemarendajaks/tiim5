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
          import('./features/dashboard/dashboard').then((m) => m.Dashboard),
      },
      {
        path: 'studies',
        loadComponent: () =>
          import('./features/studies/studies').then((m) => m.Studies),
      },
      {
        path: 'curriculums',
        children: [
          {
            path: '',
            loadComponent: () =>
              import('./features/curriculums/curriculums').then((m) => m.Curriculums),
          },
          {
            path: 'new',
            loadComponent: () =>
              import(
                './features/curriculums/curriculum-planning/curriculum-planning'
              ).then((m) => m.CurriculumPlanning),
          },
        ],
      },
      {
        path: 'schedule',
        loadComponent: () =>
          import('./features/schedule/schedule').then((m) => m.Schedule),
      },
      {
        path: 'settings',
        loadComponent: () =>
          import('./features/settings/settings').then((m) => m.Settings),
      },
      { path: '**', redirectTo: 'dashboard' },
    ],
  },
];
