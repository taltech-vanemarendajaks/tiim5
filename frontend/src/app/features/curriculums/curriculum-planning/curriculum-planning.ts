import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  Signal,
  signal,
} from '@angular/core';
import { TPipe } from '@/pipes';
import { CommonModule } from '@angular/common';
import { CourseTable, Notification, Search } from '@/components';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import {
  CourseResponse,
  CourseService,
  CurriculumService,
  PlannedCourseResponse,
  SemesterResponse,
  SemesterService,
  UserResponse,
  UserService,
} from '@/client';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { filter, map, switchMap } from 'rxjs/operators';
import { calculateSemesterCredits, findCurrentSemester, getSemesterKey } from '@/utils';
import { ActivatedRoute } from '@angular/router';

type CourseWithStatus = CourseResponse & { courseStatus: PlannedCourseResponse.courseStatus };

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
  private readonly route = inject(ActivatedRoute);
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

  protected readonly calculateSemesterCredits = calculateSemesterCredits;
  protected readonly getSemesterKey = getSemesterKey;
  readonly hideCompleted = signal(false);

  readonly semesters: Signal<SemesterResponse[]> = toSignal(
    toObservable(this.user).pipe(
      filter((user) => user !== null),
      switchMap((user) =>
        this.semesterService.getSemesters(this.studyPlanExternalId, user.externalId),
      ),
    ),
    {
      initialValue: [],
    },
  );

  getCourses(semester: SemesterResponse): CourseWithStatus[] {
    return (
      semester.plannedCourses?.map((pc) => ({
        ...pc.course,
        courseStatus: pc.courseStatus,
      })) ?? []
    );
  }

  readonly sortedSemesters = computed(() =>
    [...this.semesters()].sort((a, b) => {
      if (a.year !== b.year) return a.year - b.year;
      const order: Record<string, number> = { AUTUMN: 1, SPRING: 2 };
      return (order[a.semesterType ?? ''] ?? 0) - (order[b.semesterType ?? ''] ?? 0);
    }),
  );

  readonly filteredSemesters = computed(() =>
    this.hideCompleted()
      ? this.sortedSemesters().filter((s) => !s.finished)
      : this.sortedSemesters(),
  );
  readonly currentSemesterId = computed(() => findCurrentSemester(this.semesters()));

  readonly totalCredits = computed(() =>
    this.semesters().reduce(
      (total, s) => total + calculateSemesterCredits(s.plannedCourses ?? []),
      0,
    ),
  );

  readonly totalRequired: Signal<number | null> = toSignal(
    this.curriculumService
      .getCurriculumByStudyPlan(this.studyPlanExternalId)
      .pipe(map((curriculum) => curriculum.credits)),
    {
      initialValue: null,
    },
  );

  readonly completionPercentage = computed(() => {
    const required = this.totalRequired();
    if (!required) return null;
    return Math.round((this.totalCredits() / required) * 100);
  });
}
