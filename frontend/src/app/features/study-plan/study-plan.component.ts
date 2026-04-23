import { Component, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoursesComponent } from '../courses/courses.component';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'app-study-plan',
  standalone: true,
  imports: [CommonModule, CoursesComponent],
  templateUrl: './study-plan.component.html',
  styleUrl: './study-plan.component.css',
})
export class StudyPlanComponent {}
