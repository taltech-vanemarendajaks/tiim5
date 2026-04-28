import { TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { Dialog } from '@angular/cdk/dialog';
import { of, Subject } from 'rxjs';
import { it, expect, vi } from 'vitest';
import {
  CourseResponse,
  CourseService,
  CurriculumService,
  ModuleService,
  PlannedCourseRequest,
  PlannedCourseResponse,
  PlannedCourseService,
  SemesterResponse,
  SemesterService,
  UserService,
} from '@/client';
import { CurriculumPlanning } from './curriculum-planning';

const STUDY_PLAN_ID = 'sp-1';
const USER = { externalId: 'u-1', name: 'Jane' };

const SEMESTER_AUTUMN: SemesterResponse = {
  externalId: 'sem-autumn',
  year: 2026,
  finished: false,
  semesterType: SemesterResponse.semesterType.AUTUMN,
  creationDate: '2026-09-01T00:00:00Z',
  plannedCourses: [
    {
      externalId: 'pc-1',
      course: {
        externalId: 'c-1',
        oisExternalId: 'ois-1',
        versionExternalId: 'v-1',
        titleEn: 'Mikro',
        titleEt: 'Mikromaailma füüsika',
        code: 'LOFY.01.009',
        credits: 6,
        semesterType: CourseResponse.semesterType.AUTUMN,
      },
      module: {
        externalId: 'm-1',
        title: 'Vabaained',
        requiredCredits: 0,
        optionalCredits: 6,
      },
      courseStatus: PlannedCourseResponse.courseStatus.PLANNED,
    },
  ],
};

const SEMESTER_SPRING: SemesterResponse = {
  externalId: 'sem-spring',
  year: 2027,
  finished: false,
  semesterType: SemesterResponse.semesterType.SPRING,
  creationDate: '2027-02-01T00:00:00Z',
  plannedCourses: [],
};

function setup() {
  const semesterService = {
    getSemesters: vi.fn().mockReturnValue(of([SEMESTER_AUTUMN, SEMESTER_SPRING])),
  };
  const userService = {
    getAllUsers: vi.fn().mockReturnValue(of([USER])),
  };
  const courseService = {
    getAllCourses: vi.fn().mockReturnValue(of([])),
  };
  const curriculumService = {
    getCurriculumByStudyPlan: vi.fn().mockReturnValue(of({ credits: 120 })),
  };
  const moduleService = {
    getModules: vi.fn().mockReturnValue(of([])),
  };
  const plannedCourseService = {
    setPlannedCourses: vi.fn().mockReturnValue(of([])),
  };
  const dialogClosed$ = new Subject<string | undefined>();
  const dialog = {
    open: vi.fn().mockReturnValue({ closed: dialogClosed$.asObservable() }),
  };

  TestBed.configureTestingModule({
    providers: [
      {
        provide: ActivatedRoute,
        useValue: { snapshot: { params: { externalId: STUDY_PLAN_ID } } },
      },
      { provide: UserService, useValue: userService },
      { provide: CourseService, useValue: courseService },
      { provide: CurriculumService, useValue: curriculumService },
      { provide: ModuleService, useValue: moduleService },
      { provide: SemesterService, useValue: semesterService },
      { provide: PlannedCourseService, useValue: plannedCourseService },
      { provide: Dialog, useValue: dialog },
    ],
  });

  const fixture = TestBed.createComponent(CurriculumPlanning);
  fixture.detectChanges();
  return { fixture, plannedCourseService, dialog, dialogClosed$ };
}

const SPRING_COURSE: CourseResponse = {
  externalId: undefined as unknown as string,
  oisExternalId: 'ois-2',
  versionExternalId: 'v-2',
  titleEn: 'Algebra',
  titleEt: 'Algebra',
  code: 'MTAT.02',
  credits: 4,
  semesterType: CourseResponse.semesterType.SPRING,
};

it('hydrates draft from server semesters and is not dirty initially', () => {
  const { fixture } = setup();
  const cmp = fixture.componentInstance;
  expect(cmp.draftPlannedCourses().length).toBe(1);
  expect(cmp.dirty()).toBe(false);
});

it('opens the dialog with only matching-type semesters and adds to draft on confirm', () => {
  const { fixture, dialog, dialogClosed$ } = setup();
  const cmp = fixture.componentInstance;

  cmp.addCourse(SPRING_COURSE);

  expect(dialog.open).toHaveBeenCalledTimes(1);
  const passed = dialog.open.mock.calls[0][1].data.semesters as SemesterResponse[];
  expect(passed.map((s) => s.externalId)).toEqual(['sem-spring']);

  dialogClosed$.next('sem-spring');

  const draft = cmp.draftPlannedCourses();
  expect(draft.length).toBe(2);
  expect(draft.some((d) => d.course.versionExternalId === 'v-2')).toBe(true);
  expect(cmp.dirty()).toBe(true);
});

it('shows error notification when the only matching semester already contains this course', () => {
  const { fixture, dialog, dialogClosed$ } = setup();
  const cmp = fixture.componentInstance;

  cmp.addCourse(SPRING_COURSE);
  dialogClosed$.next('sem-spring');

  dialog.open.mockClear();
  cmp.addCourse(SPRING_COURSE);

  expect(dialog.open).not.toHaveBeenCalled();
  expect(cmp.notification()).toEqual({
    kind: 'error',
    key: 'CurriculumPlanning.NoMatchingSemesters',
  });
});

it('removes a draft course and toggles status', () => {
  const { fixture } = setup();
  const cmp = fixture.componentInstance;

  cmp.toggleCompleted('v-1', 'sem-autumn', true);
  expect(cmp.draftPlannedCourses()[0].status).toBe(PlannedCourseResponse.courseStatus.COMPLETED);
  expect(cmp.dirty()).toBe(true);

  cmp.toggleCompleted('v-1', 'sem-autumn', false);
  expect(cmp.draftPlannedCourses()[0].status).toBe(PlannedCourseResponse.courseStatus.PLANNED);
  expect(cmp.dirty()).toBe(false);

  cmp.removeCourse('v-1', 'sem-autumn');
  expect(cmp.draftPlannedCourses().length).toBe(0);
  expect(cmp.dirty()).toBe(true);
});

it('serializes the full draft as PlannedCourseRequest[] on save', () => {
  const { fixture, plannedCourseService } = setup();
  const cmp = fixture.componentInstance;

  cmp.toggleCompleted('v-1', 'sem-autumn', true);
  cmp.save();

  expect(plannedCourseService.setPlannedCourses).toHaveBeenCalledTimes(1);
  const [studyPlanId, payload] = plannedCourseService.setPlannedCourses.mock.calls[0];
  expect(studyPlanId).toBe(STUDY_PLAN_ID);
  expect(payload).toEqual<PlannedCourseRequest[]>([
    {
      semesterExternalId: 'sem-autumn',
      courseVersionExternalId: 'v-1',
      courseExternalId: 'ois-1',
      status: PlannedCourseRequest.status.COMPLETED,
    },
  ]);
});

it('does not call the service when not dirty', () => {
  const { fixture, plannedCourseService } = setup();
  fixture.componentInstance.save();
  expect(plannedCourseService.setPlannedCourses).not.toHaveBeenCalled();
});
