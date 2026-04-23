import { Component, input, ChangeDetectionStrategy } from '@angular/core';
import { MatChipsModule } from '@angular/material/chips';
import { CourseResponse } from '@/client';

const SEMESTER_MAP: Record<CourseResponse.semesterType, { label: string; color: string }> = {
  [CourseResponse.semesterType.SPRING]: { label: 'S', color: '#85DC75' },
  [CourseResponse.semesterType.AUTUMN]: { label: 'K', color: '#CAE0FF' },
};

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'app-semester-badge',
  standalone: true,
  imports: [MatChipsModule],
  templateUrl: './semester-badge.component.html',
  styleUrl: './semester-badge.component.css',
})
export class SemesterBadgeComponent {
  readonly semester = input.required<CourseResponse.semesterType>();

  get badge() {
    return SEMESTER_MAP[this.semester()];
  }
}
