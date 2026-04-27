import { Component, input, ChangeDetectionStrategy, TemplateRef } from '@angular/core';
import { CourseResponse } from '@/client';
import { TPipe } from '@/pipes';
import { NgTemplateOutlet } from '@angular/common';
import { SemesterBadge } from '../semester-badge/semester-badge';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'app-course-table',
  standalone: true,
  imports: [SemesterBadge, TPipe, NgTemplateOutlet],
  templateUrl: './course-table.html',
  styleUrl: './course-table.css',
})
export class CourseTable {
  readonly showHeader = input<boolean>(true);
  readonly actionTemplate = input<TemplateRef<{ $implicit: CourseResponse }> | null>(null);
  readonly courses = input.required<CourseResponse[]>();
}
