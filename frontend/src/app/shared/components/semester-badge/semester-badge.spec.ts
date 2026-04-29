import { TestBed } from '@angular/core/testing';
import { Component, ChangeDetectionStrategy } from '@angular/core';
import { SemesterBadge } from './semester-badge';
import { it, expect } from 'vitest';
import { CourseResponse } from '@/client';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
  imports: [SemesterBadge],
  template: `<app-semester-badge [semester]="semester" />`,
})
class TestHostComponent {
  semester = CourseResponse.semesterType.SPRING;
}

it('should display "K" label for SPRING (kevad)', () => {
  const fixture = TestBed.createComponent(TestHostComponent);
  fixture.detectChanges();

  const chip = fixture.nativeElement.querySelector('mat-chip');
  expect(chip.textContent.trim()).toBe('K');
});

it('should display "S" label for AUTUMN (sügis)', () => {
  const fixture = TestBed.createComponent(TestHostComponent);
  fixture.componentInstance.semester = CourseResponse.semesterType.AUTUMN;
  fixture.detectChanges();

  const chip = fixture.nativeElement.querySelector('mat-chip');
  expect(chip.textContent.trim()).toBe('S');
});
