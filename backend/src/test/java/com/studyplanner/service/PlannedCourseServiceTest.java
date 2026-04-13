package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import com.studyplanner.client.OisClient;
import com.studyplanner.entity.CourseStatus;
import com.studyplanner.repository.*;
import com.studyplanner.utils.UserRequestContext;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlannedCourseServiceTest {

  @Mock private SemesterService semesterService;
  @Mock private StudyPlanService studyPlanService;
  @Mock private CourseService courseService;
  @Mock private ModuleService moduleService;
  @Mock private PlannedCourseRepository plannedCourseRepository;
  @Mock private SemesterRepository semesterRepository;
  @Mock private OisClient oisClient;
  @InjectMocks private PlannedCourseService plannedCourseService;

  @Test
  void setPlannedCoursesTest() {
    try (var mockedRequestContext = mockStatic(UserRequestContext.class)) {
      mockedRequestContext
          .when(UserRequestContext::getUserExternalId)
          .thenReturn(A_USER_EXTERNAL_ID);

      when(studyPlanService.fetchStudyPlanById(A_STUDY_PLAN_EXTERNAL_ID)).thenReturn(aStudyPlan());
      when(semesterService.fetchSemestersByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(List.of(aSemester()));
      when(courseService.fetchCoursesByVersionExternalIds(any())).thenReturn(List.of(aCourse()));
      when(moduleService.fetchModulesByCourseIds(any())).thenReturn(List.of(aModule()));
      when(moduleService.fetchModuleByTitleAndCurriculumExternalId(any(), any()))
          .thenReturn(aModule("Vabaained"));

      var actual =
          plannedCourseService.setPlannedCourses(
              A_STUDY_PLAN_EXTERNAL_ID, List.of(aPlannedCourseRequest()));

      assertThat(actual).hasSize(1);
      verify(oisClient, never()).getCourseByVersionExternalId(any(), any());
      verify(plannedCourseRepository).deleteByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID);
      verify(plannedCourseRepository).saveAll(anyList());
    }
  }

  @Test
  void shouldFetchCourseWhenNotFoundLocally() {
    var request = aPlannedCourseRequest();

    try (var mockedRequestContext = mockStatic(UserRequestContext.class)) {
      mockedRequestContext
          .when(UserRequestContext::getUserExternalId)
          .thenReturn(A_USER_EXTERNAL_ID);

      when(studyPlanService.fetchStudyPlanById(A_STUDY_PLAN_EXTERNAL_ID)).thenReturn(aStudyPlan());
      when(semesterService.fetchSemestersByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(List.of(aSemester()));
      when(courseService.fetchCoursesByVersionExternalIds(any())).thenReturn(List.of());
      when(courseService.getFromOisAndSaveCourse(
              request.courseExternalId(), request.courseVersionExternalId()))
          .thenReturn(aCourse());
      when(moduleService.fetchModulesByCourseIds(any())).thenReturn(List.of());
      when(moduleService.fetchModuleByTitleAndCurriculumExternalId(any(), any()))
          .thenReturn(aModule("Vabaained"));

      plannedCourseService.setPlannedCourses(A_STUDY_PLAN_EXTERNAL_ID, List.of(request));

      verify(courseService)
          .getFromOisAndSaveCourse(request.courseExternalId(), request.courseVersionExternalId());
    }
  }

  @Test
  void shouldUpdateCourseStatusToCompleted() {

    var plannedCourse = aPlannedCourse();
    var semester = plannedCourse.getSemester();
    semester.setPlannedCourses(List.of(plannedCourse));

    when(plannedCourseRepository.findByExternalIdWithSemesterAndCourses(AN_EXTERNAL_ID))
        .thenReturn(Optional.of(plannedCourse));

    plannedCourseService.updateStatus(AN_EXTERNAL_ID, CourseStatus.COMPLETED);

    assertThat(plannedCourse.getStatus()).isEqualTo(CourseStatus.COMPLETED);
    assertThat(semester.getFinished()).isTrue();
    verify(semesterRepository).save(semester);
  }
}
