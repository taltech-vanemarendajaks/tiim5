import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/study-plan/study-plan.component').then((m) => m.StudyPlanComponent),
  },
];
