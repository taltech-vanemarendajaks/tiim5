import { TestBed } from '@angular/core/testing';
import { DIALOG_DATA, DialogRef } from '@angular/cdk/dialog';
import { it, expect, vi } from 'vitest';
import { AddCoursePlanDialog, AddCoursePlanDialogData } from './add-course-plan-dialog';
import { CourseResponse, SemesterResponse } from '@/client';

const course: CourseResponse = {
  externalId: 'c-1',
  oisExternalId: 'ois-1',
  versionExternalId: 'v-1',
  titleEn: 'Higher Math',
  titleEt: 'Kõrgem matemaatika',
  code: 'MTAT.01',
  credits: 6,
  semesterType: CourseResponse.semesterType.AUTUMN,
};

const semesters: SemesterResponse[] = [
  {
    externalId: 's-1',
    year: 2025,
    finished: false,
    semesterType: SemesterResponse.semesterType.AUTUMN,
    creationDate: '2025-09-01T00:00:00Z',
  },
  {
    externalId: 's-2',
    year: 2026,
    finished: false,
    semesterType: SemesterResponse.semesterType.AUTUMN,
    creationDate: '2026-09-01T00:00:00Z',
  },
];

function setup(data: AddCoursePlanDialogData = { course, semesters }) {
  const close = vi.fn();
  TestBed.configureTestingModule({
    providers: [
      { provide: DIALOG_DATA, useValue: data },
      { provide: DialogRef, useValue: { close } },
    ],
  });
  const fixture = TestBed.createComponent(AddCoursePlanDialog);
  fixture.detectChanges();
  return { fixture, close };
}

it('renders course title and a select with all semesters', () => {
  const { fixture } = setup();
  const heading = fixture.nativeElement.querySelector('.dialog__course-title');
  expect(heading.textContent.trim()).toBe('Kõrgem matemaatika');

  const options = fixture.nativeElement.querySelectorAll('option');
  expect(options.length).toBe(2);
});

it('closes with the first semester id when submit is clicked without changing selection', () => {
  const { fixture, close } = setup();
  const submit: HTMLButtonElement = fixture.nativeElement.querySelector('.dialog__submit');
  submit.click();
  expect(close).toHaveBeenCalledWith('s-1');
});

it('closes with the picked semester id after selection change', () => {
  const { fixture, close } = setup();
  const select: HTMLSelectElement = fixture.nativeElement.querySelector('.dialog__select');
  select.value = 's-2';
  select.dispatchEvent(new Event('change'));
  fixture.detectChanges();

  const submit: HTMLButtonElement = fixture.nativeElement.querySelector('.dialog__submit');
  submit.click();
  expect(close).toHaveBeenCalledWith('s-2');
});

it('closes with undefined when X is clicked', () => {
  const { fixture, close } = setup();
  const cancel: HTMLButtonElement = fixture.nativeElement.querySelector('.dialog__close');
  cancel.click();
  expect(close).toHaveBeenCalledWith(undefined);
});

it('disables submit and shows fallback message when no matching semesters', () => {
  const { fixture } = setup({ course, semesters: [] });
  const submit: HTMLButtonElement = fixture.nativeElement.querySelector('.dialog__submit');
  expect(submit.disabled).toBe(true);

  const fallback = fixture.nativeElement.querySelector('.dialog__no-options');
  expect(fallback).not.toBeNull();
});
