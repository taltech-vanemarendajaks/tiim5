import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { Button } from 'primeng/button';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { combineLatest, debounceTime, filter, startWith, switchMap, tap } from 'rxjs';
import { UserService, CurriculumService } from '@/client';
import { CurriculumResponse, CurriculumVersionResponse } from '@/client';
import { TPipe } from '@/pipes';
import { I18nService, UserIdentityService } from '@/services';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, InputText, Select, Button, TPipe],
  templateUrl: './register.html',
  styleUrl: './register.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Register {
  private readonly fb = inject(FormBuilder);
  private readonly userService = inject(UserService);
  private readonly curriculumService = inject(CurriculumService);
  private readonly userIdentity = inject(UserIdentityService);
  private readonly i18n = inject(I18nService);

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
    name: ['', Validators.required],
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
    this.userService
      .createNewUser({
        name: this.form.value.name!,
        studyLevel: this.form.value.studyLevel!,
        curriculumVersionId: this.form.value.curriculumVersionExternalId!,
        curriculumId: this.form.value.curriculumExternalId!,
      })
      .subscribe({
        next: (response) => {
          this.userIdentity.set(response.externalId);
          this.submitted.set(true);
          this.loading.set(false);
          window.location.href = '/curriculums';
        },
        error: () => {
          this.loading.set(false);
        },
      });
  }
}
