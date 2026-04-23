import { Component, ChangeDetectionStrategy, inject, signal, Signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationComponent, SearchComponent, SemesterBadgeComponent } from '@/components';
import { CourseControllerService, CourseResponse } from '../../../client/_generated_';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { filter, switchMap } from 'rxjs/operators';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'app-study-plan',
  standalone: true,
  imports: [CommonModule, NotificationComponent, SearchComponent, SemesterBadgeComponent],
  templateUrl: './study-plan.component.html',
  styleUrl: './study-plan.component.css',
})
export class StudyPlanComponent {
  private readonly courseService = inject(CourseControllerService);

  readonly searchTitle = signal<string>('');

  readonly courses: Signal<CourseResponse[]> = toSignal(
    toObservable(this.searchTitle).pipe(
      filter((title) => title.length === 0 || title.length > 2),
      switchMap((title) => this.courseService.getAllCourses(undefined, 10, title)),
    ),
    { initialValue: [] },
  );
}
