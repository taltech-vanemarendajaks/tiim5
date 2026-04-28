import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  linkedSignal,
  Signal,
  signal,
} from '@angular/core';
import { TPipe } from '@/pipes';
import { CommonModule } from '@angular/common';
import { CourseTable, Notification, Search } from '@/components';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { Dialog } from '@angular/cdk/dialog';
import {
  CourseResponse,
  CourseService,
  CurriculumService,
  ModuleResponse,
  ModuleService,
  PlannedCourseRequest,
  PlannedCourseResponse,
  PlannedCourseService,
  SemesterResponse,
  SemesterService,
  UserResponse,
  UserService,
} from '@/client';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { catchError, filter, map, switchMap } from 'rxjs/operators';
import { combineLatest, of } from 'rxjs';
import { findCurrentSemester, getSemesterKey } from '@/utils';
import { ActivatedRoute } from '@angular/router';
import {
  AddCoursePlanDialog,
  AddCoursePlanDialogData,
} from './add-course-plan-dialog/add-course-plan-dialog';

type CourseWithStatus = CourseResponse & { courseStatus: PlannedCourseResponse.courseStatus };

interface DraftPlannedCourse {
  externalId?: string;
  semesterExternalId: string;
  course: CourseResponse;
  module?: ModuleResponse;
  status: PlannedCourseResponse.courseStatus;
}

interface SaveNotification {
  kind: 'success' | 'error';
  key: string;
}

@Component({
  selector: 'app-curriculum-planning',
  imports: [TPipe, CommonModule, Notification, Search, CdkAccordionModule, CourseTable],
  templateUrl: './curriculum-planning.html',
  styleUrl: './curriculum-planning.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CurriculumPlanning {
  private readonly courseService = inject(CourseService);
  private readonly semesterService = inject(SemesterService);
  private readonly curriculumService = inject(CurriculumService);
  private readonly userService = inject(UserService);
  private readonly moduleService = inject(ModuleService);
  private readonly plannedCourseService = inject(PlannedCourseService);
  private readonly route = inject(ActivatedRoute);
  private readonly dialog = inject(Dialog);
  private readonly studyPlanExternalId = this.route.snapshot.params['externalId'];

  readonly user: Signal<UserResponse | null> = toSignal(
    this.userService.getAllUsers().pipe(map((users) => users[0] ?? null)),
    { initialValue: null },
  );

  readonly searchTitle = signal<string>('');
  readonly searchTitle$ = toObservable(this.searchTitle);

  readonly courses$ = this.searchTitle$.pipe(
    filter((title) => title.length === 0 || title.length > 2),
    switchMap((title) => this.courseService.getAllCourses(undefined, 10, title)),
  );

  readonly courses = toSignal(this.courses$, { initialValue: [] });

  protected readonly getSemesterKey = getSemesterKey;
  readonly hideCompleted = signal(false);

  private readonly refreshTrigger = signal(0);
  readonly saving = signal(false);
  readonly notification = signal<SaveNotification | null>(null);

  readonly semesters: Signal<SemesterResponse[]> = toSignal(
    combineLatest([toObservable(this.user), toObservable(this.refreshTrigger)]).pipe(
      filter(([user]) => user !== null),
      switchMap(([user]) =>
        this.semesterService.getSemesters(this.studyPlanExternalId, user!.externalId),
      ),
    ),
    { initialValue: [] },
  );

  readonly draftPlannedCourses = linkedSignal<DraftPlannedCourse[]>(() =>
    this.flattenSemesters(this.semesters()),
  );

  private readonly initialPlannedCourses = linkedSignal<DraftPlannedCourse[]>(() =>
    this.flattenSemesters(this.semesters()),
  );

  readonly dirty = computed(
    () =>
      this.serializeDraft(this.draftPlannedCourses()) !==
      this.serializeDraft(this.initialPlannedCourses()),
  );

  readonly sortedSemesters = computed(() =>
    [...this.semesters()].sort((a, b) => {
      if (a.year !== b.year) return a.year - b.year;
      const order: Record<string, number> = { SPRING: 1, AUTUMN: 2 };
      return (order[a.semesterType ?? ''] ?? 0) - (order[b.semesterType ?? ''] ?? 0);
    }),
  );

  readonly filteredSemesters = computed(() =>
    this.hideCompleted()
      ? this.sortedSemesters().filter((s) => !this.isSemesterFinished(s.externalId))
      : this.sortedSemesters(),
  );

  isSemesterFinished(semesterExternalId: string): boolean {
    const courses = this.draftPlannedCourses().filter(
      (d) => d.semesterExternalId === semesterExternalId,
    );
    if (courses.length === 0) return false;
    return courses.every((d) => d.status === PlannedCourseResponse.courseStatus.COMPLETED);
  }

  readonly currentSemesterId = computed(() => findCurrentSemester(this.semesters()));

  readonly totalCredits = computed(() =>
    this.draftPlannedCourses().reduce((sum, d) => sum + (d.course.credits ?? 0), 0),
  );

  readonly totalRequired: Signal<number | null> = toSignal(
    this.curriculumService.getCurriculumByStudyPlan(this.studyPlanExternalId).pipe(
      map((curriculum) => curriculum.credits),
      catchError(() => of(null)),
    ),
    { initialValue: null },
  );

  readonly completionPercentage = computed(() => {
    const required = this.totalRequired();
    if (!required) return null;
    return Math.round((this.totalCredits() / required) * 100);
  });

  readonly modules: Signal<ModuleResponse[]> = toSignal(
    this.moduleService.getModules(this.studyPlanExternalId),
    { initialValue: [] },
  );

  readonly moduleCredits: Signal<Map<string, number>> = computed(() => {
    const map = new Map<string, number>();
    this.draftPlannedCourses().forEach((d) => {
      if (d.status === PlannedCourseResponse.courseStatus.COMPLETED && d.module) {
        const moduleId = d.module.externalId;
        map.set(moduleId, (map.get(moduleId) ?? 0) + (d.course.credits ?? 0));
      }
    });
    return map;
  });

  getCourses(semester: SemesterResponse): CourseWithStatus[] {
    return this.draftPlannedCourses()
      .filter((d) => d.semesterExternalId === semester.externalId)
      .map((d) => ({ ...d.course, courseStatus: d.status }));
  }

  getSemesterCredits(semesterExternalId: string): number {
    return this.draftPlannedCourses()
      .filter((d) => d.semesterExternalId === semesterExternalId)
      .reduce((sum, d) => sum + (d.course.credits ?? 0), 0);
  }

  addCourse(course: CourseResponse): void {
    const draft = this.draftPlannedCourses();
    const matching = this.sortedSemesters().filter(
      (s) =>
        s.semesterType !== undefined &&
        (s.semesterType as string) === (course.semesterType as string) &&
        !draft.some(
          (d) =>
            d.semesterExternalId === s.externalId &&
            d.course.versionExternalId === course.versionExternalId,
        ),
    );

    if (matching.length === 0) {
      this.notification.set({ kind: 'error', key: 'CurriculumPlanning.NoMatchingSemesters' });
      return;
    }

    const data: AddCoursePlanDialogData = { course, semesters: matching };
    const ref = this.dialog.open<string | undefined>(AddCoursePlanDialog, { data });
    ref.closed.subscribe((semesterExternalId) => {
      if (!semesterExternalId) return;
      this.draftPlannedCourses.update((items) => [
        ...items,
        {
          semesterExternalId,
          course,
          status: PlannedCourseResponse.courseStatus.PLANNED,
        },
      ]);
    });
  }

  removeCourse(courseVersionExternalId: string, semesterExternalId: string): void {
    this.draftPlannedCourses.update((items) =>
      items.filter(
        (d) =>
          !(
            d.semesterExternalId === semesterExternalId &&
            d.course.versionExternalId === courseVersionExternalId
          ),
      ),
    );
  }

  toggleCompleted(
    courseVersionExternalId: string,
    semesterExternalId: string,
    checked: boolean,
  ): void {
    const next = checked
      ? PlannedCourseResponse.courseStatus.COMPLETED
      : PlannedCourseResponse.courseStatus.PLANNED;
    this.draftPlannedCourses.update((items) =>
      items.map((d) =>
        d.semesterExternalId === semesterExternalId &&
        d.course.versionExternalId === courseVersionExternalId
          ? { ...d, status: next }
          : d,
      ),
    );
  }

  save(): void {
    if (!this.dirty() || this.saving()) return;
    this.saving.set(true);
    this.notification.set(null);

    const requests: PlannedCourseRequest[] = this.draftPlannedCourses().map((d) => ({
      semesterExternalId: d.semesterExternalId,
      courseVersionExternalId: d.course.versionExternalId,
      courseExternalId: d.course.oisExternalId,
      status: d.status as unknown as PlannedCourseRequest.status,
    }));

    this.plannedCourseService
      .setPlannedCourses(this.studyPlanExternalId, requests, this.user()?.externalId)
      .pipe(
        catchError(() => {
          this.notification.set({ kind: 'error', key: 'CurriculumPlanning.SaveError' });
          this.saving.set(false);
          return of(null);
        }),
      )
      .subscribe((result) => {
        if (result === null) return;
        this.saving.set(false);
        this.notification.set({ kind: 'success', key: 'CurriculumPlanning.SaveSuccess' });
        this.refreshTrigger.update((n) => n + 1);
      });
  }

  dismissNotification(): void {
    this.notification.set(null);
  }

  private flattenSemesters(semesters: SemesterResponse[]): DraftPlannedCourse[] {
    return semesters.flatMap((s) =>
      (s.plannedCourses ?? []).map((pc) => ({
        externalId: pc.externalId,
        semesterExternalId: s.externalId,
        course: pc.course,
        module: pc.module,
        status: pc.courseStatus,
      })),
    );
  }

  private serializeDraft(items: DraftPlannedCourse[]): string {
    return JSON.stringify(
      [...items]
        .map((d) => ({
          s: d.semesterExternalId,
          v: d.course.versionExternalId,
          st: d.status,
        }))
        .sort((a, b) => (a.s + a.v).localeCompare(b.s + b.v)),
    );
  }
}
