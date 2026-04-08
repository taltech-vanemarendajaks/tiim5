package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.studyplanner.client.OisClient;
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

  @Mock private SemesterRepository semesterRepository;
  @Mock private StudyPlanRepository studyPlanRepository;
  @Mock private CourseRepository courseRepository;
  @Mock private ModuleRepository moduleRepository;
  @Mock private PlannedCourseRepository plannedCourseRepository;
  @Mock private OisClient oisClient;
  @InjectMocks private PlannedCourseService plannedCourseService;

  @Test
  void setPlannedCoursesTest() {
    try (var mockedRequestContext = mockStatic(UserRequestContext.class)) {
      mockedRequestContext
          .when(UserRequestContext::getUserExternalId)
          .thenReturn(A_USER_EXTERNAL_ID);

      when(studyPlanRepository.findByExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(Optional.of(aStudyPlan()));
      when(semesterRepository.findAllByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(List.of(aSemester()));
      when(courseRepository.findAllByCourseVersionExternalIdIn(any()))
          .thenReturn(List.of(aCourse()));
      when(moduleRepository.findModulesWithCurriculums(any())).thenReturn(List.of(aModule()));
      when(moduleRepository.findByTitleAndCurriculums_ExternalId(any(), any()))
          .thenReturn(Optional.of(aModule("Vabaained")));

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

      when(studyPlanRepository.findByExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(Optional.of(aStudyPlan()));
      when(semesterRepository.findAllByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(List.of(aSemester()));
      when(courseRepository.findAllByCourseVersionExternalIdIn(any())).thenReturn(List.of());
      when(oisClient.getCourseByVersionExternalId(any(), any()))
          .thenReturn(aOisCourseFullResponse());
      when(courseRepository.save(any())).thenReturn(aCourse());
      when(moduleRepository.findModulesWithCurriculums(any())).thenReturn(List.of());
      when(moduleRepository.findByTitleAndCurriculums_ExternalId(any(), any()))
          .thenReturn(Optional.of(aModule("Vabaained")));

      plannedCourseService.setPlannedCourses(A_STUDY_PLAN_EXTERNAL_ID, List.of(request));

      verify(oisClient)
          .getCourseByVersionExternalId(
              request.courseExternalId(), request.courseVersionExternalId());
      verify(courseRepository)
          .save(
              argThat(course -> course.getCourseVersionExternalId().equals(A_LATEST_VERSION_UUID)));
    }
  }
}
