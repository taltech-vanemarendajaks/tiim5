import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { TPipe } from '@/pipes';
import { CommonModule } from '@angular/common';
import { Notification, Search, SemesterBadge } from '@/components';
import { CourseService } from '@/client';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { filter, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-curriculum-planning',
  imports: [TPipe, CommonModule, Notification, Search, SemesterBadge],
  templateUrl: './curriculum-planning.html',
  styleUrl: './curriculum-planning.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CurriculumPlanning {
  private readonly courseService = inject(CourseService);

  readonly searchTitle = signal<string>('');
  readonly searchTitle$ = toObservable(this.searchTitle);

  readonly courses$ = this.searchTitle$.pipe(
    filter((title) => title.length === 0 || title.length > 2),
    switchMap((title) => this.courseService.getAllCourses(undefined, 10, title)),
  );

  readonly courses = toSignal(this.courses$, { initialValue: [] });
}
