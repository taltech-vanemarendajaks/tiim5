import { Component, inject, ChangeDetectionStrategy, Signal, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { CourseControllerService, CourseResponse } from '../../../client/_generated_';
import { NotificationComponent, SemesterBadgeComponent, SearchComponent } from '@/components';
import { filter, switchMap } from 'rxjs/operators';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'app-courses',
  standalone: true,
  imports: [CommonModule, SemesterBadgeComponent, NotificationComponent, SearchComponent],
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.css',
})
export class CoursesComponent {
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
