import { Component, inject, ChangeDetectionStrategy, Signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { CourseControllerService, CourseResponse } from '../../../client/_generated_';
import { SemesterBadgeComponent } from '../../shared/components/SemesterBadge/semester-badge.component';
import { NotificationComponent } from '../../shared/components/Notification/notification.component';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'app-courses',
  standalone: true,
  imports: [CommonModule, SemesterBadgeComponent, NotificationComponent],
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.css',
})
export class CoursesComponent {
  private courseService = inject(CourseControllerService);

  readonly courses: Signal<CourseResponse[]> = toSignal(
    this.courseService.getAllCourses(),
    {
      initialValue: [],
    },
  );
}
