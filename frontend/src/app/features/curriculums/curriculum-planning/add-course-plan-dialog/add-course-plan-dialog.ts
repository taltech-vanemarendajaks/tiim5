import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { DIALOG_DATA, DialogRef } from '@angular/cdk/dialog';
import { CommonModule } from '@angular/common';
import { TPipe } from '@/pipes';
import { CourseResponse, SemesterResponse } from '@/client';
import { getSemesterKey } from '@/utils';

export interface AddCoursePlanDialogData {
  course: CourseResponse;
  semesters: SemesterResponse[];
}

@Component({
  selector: 'app-add-course-plan-dialog',
  imports: [CommonModule, TPipe],
  templateUrl: './add-course-plan-dialog.html',
  styleUrl: './add-course-plan-dialog.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddCoursePlanDialog {
  private readonly dialogRef = inject<DialogRef<string | undefined>>(DialogRef);
  protected readonly data = inject<AddCoursePlanDialogData>(DIALOG_DATA);
  protected readonly getSemesterKey = getSemesterKey;
  protected readonly selectedSemesterId = signal<string>(this.data.semesters[0]?.externalId ?? '');

  protected onSemesterChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedSemesterId.set(value);
  }

  protected confirm(): void {
    const id = this.selectedSemesterId();
    if (!id) return;
    this.dialogRef.close(id);
  }

  protected cancel(): void {
    this.dialogRef.close(undefined);
  }
}
