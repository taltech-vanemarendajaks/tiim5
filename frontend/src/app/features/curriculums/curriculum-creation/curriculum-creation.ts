import { ChangeDetectionStrategy, Component, inject, signal, Signal } from '@angular/core';
import { TPipe } from '@/pipes';
import {
  CurriculumResponse,
  CurriculumService,
  CurriculumVersionResponse,
  StudyPlanService,
  UserResponse,
  UserService,
} from '@/client';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { filter, switchMap } from 'rxjs/operators';
import { combineLatest, debounceTime, startWith, tap } from 'rxjs';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Select } from 'primeng/select';
import { Button } from 'primeng/button';
import { I18nService } from '@/services';

@Component({
  selector: 'app-curriculum-creation',
  imports: [TPipe, ReactiveFormsModule, Select, Button],
  templateUrl: './curriculum-creation.html',
  styleUrl: './curriculum-creation.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CurriculumCreation {
  private readonly studyPlanService = inject(StudyPlanService);
  private readonly userService = inject(UserService);
  private readonly i18n = inject(I18nService);

  readonly user: Signal<UserResponse | null> = toSignal(this.userService.getCurrentUser(), {
    initialValue: null,
  });

  private readonly fb = inject(FormBuilder);
  private readonly curriculumService = inject(CurriculumService);

  readonly loading = signal(false);
  readonly submitted = signal(false);

  readonly studyLevelOptions = [
    {
      label: this.i18n.translate('Curriculums.Bachelor'),
      value: CurriculumResponse.studyLevel.BACHELOR,
    },
    {
      label: this.i18n.translate('Curriculums.Master'),
      value: CurriculumResponse.studyLevel.MASTER,
    },
    {
      label: this.i18n.translate('Curriculums.Integrated'),
      value: CurriculumResponse.studyLevel.INTEGRATED,
    },
    {
      label: this.i18n.translate('Curriculums.Doctor'),
      value: CurriculumResponse.studyLevel.DOCTOR,
    },
  ];

  readonly form = this.fb.group({
    studyLevel: ['', Validators.required],
    curriculumExternalId: ['', Validators.required],
    curriculumVersionExternalId: ['', Validators.required],
  });

  readonly searchCurriculumContent = signal<string>('');
  readonly searchCurriculumVersionContent = signal<string>('');

  readonly curriculums = toSignal(
    combineLatest([
      toObservable(this.searchCurriculumContent).pipe(debounceTime(300)),
      this.form.controls.studyLevel.valueChanges.pipe(startWith('')),
    ]).pipe(
      filter(([q, studyLevel]) => !!studyLevel && q.length > 3),
      switchMap(([q, studyLevel]) =>
        this.curriculumService.getAllCurriculums(1, 24, q || undefined, studyLevel!),
      ),
    ),
    { initialValue: [] as CurriculumResponse[] },
  );

  readonly curriculumVersions = toSignal(
    this.form.controls.curriculumExternalId.valueChanges.pipe(
      tap(() => this.form.controls.curriculumVersionExternalId.reset()),
      filter((id): id is string => !!id),
      switchMap((id) => this.curriculumService.getVersionsForCurriculum(id)),
    ),
    { initialValue: [] as CurriculumVersionResponse[] },
  );

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.studyPlanService
      .addNewStudyPlan({
        curriculumVersionId: this.form.value.curriculumVersionExternalId!,
        curriculumId: this.form.value.curriculumExternalId!,
      })
      .subscribe({
        next: () => {
          this.submitted.set(true);
          this.loading.set(false);
          window.location.href = "/curriculums"
        },
        error: () => {
          this.loading.set(false);
        },
      });
  }
}
