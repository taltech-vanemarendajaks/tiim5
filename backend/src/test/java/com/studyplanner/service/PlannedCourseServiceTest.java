package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.studyplanner.client.OisClient;
import com.studyplanner.client.dto.OisCourseFullResponse;
import com.studyplanner.entity.*;
import com.studyplanner.entity.Module;
import com.studyplanner.repository.*;
import com.studyplanner.utils.UserRequestContext;
import java.util.ArrayList;
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
  void updatePlannedCoursesTest() {
    var course = aCourse();
    var request = List.of(aPlannedCourseRequest());
    try (var mockedRequestContext = mockStatic(UserRequestContext.class)) {
      mockedRequestContext
          .when(UserRequestContext::getUserExternalId)
          .thenReturn(A_USER_EXTERNAL_ID);

      when(studyPlanRepository.findByExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(Optional.ofNullable(aStudyPlan()));
      when(semesterRepository.findAllByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(List.of(aSemester()));
      when(courseRepository.findByCourseVersionExternalId(A_LATEST_VERSION_UUID))
          .thenReturn(Optional.of(course));
      when(moduleRepository.findByCourseId(course.getId())).thenReturn(List.of(aModule()));

      var actual = plannedCourseService.updatePlannedCourses(A_STUDY_PLAN_EXTERNAL_ID, request);

      assertThat(actual).hasSize(1);
      verify(plannedCourseRepository).deleteByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID);
      verify(plannedCourseRepository).saveAll(anyList());
    }
  }

  @Test
  void resolveCourse_courseExistsInDb_doesNotCallExternalApi() {
    var request = List.of(aPlannedCourseRequest());
    try (var mocked = mockStatic(UserRequestContext.class)) {
      mocked.when(UserRequestContext::getUserExternalId).thenReturn(A_USER_EXTERNAL_ID);

      when(studyPlanRepository.findByExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(Optional.of(aStudyPlan()));
      when(semesterRepository.findAllByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(List.of(aSemester()));
      when(courseRepository.findByCourseVersionExternalId(A_LATEST_VERSION_UUID))
          .thenReturn(Optional.of(aCourse()));
      when(moduleRepository.findByCourseId(aCourse().getId())).thenReturn(List.of(aModule()));

      plannedCourseService.updatePlannedCourses(A_STUDY_PLAN_EXTERNAL_ID, request);

      verifyNoInteractions(oisClient);
    }
  }

  @Test
  void resolveCourse_courseNotInDb_fetchesFromExternalApiAndSavesToOptionalSubjects() {
    var course = aCourse();
    var optionalSubjectsModule =
        Module.builder()
            .id(99L)
            .courses(new ArrayList<>())
            .curriculums(List.of(aCurriculum()))
            .build();

    try (var mocked = mockStatic(UserRequestContext.class)) {
      mocked.when(UserRequestContext::getUserExternalId).thenReturn(A_USER_EXTERNAL_ID);

      when(studyPlanRepository.findByExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(Optional.of(aStudyPlan()));
      when(semesterRepository.findAllByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(List.of(aSemester()));
      when(courseRepository.findByCourseVersionExternalId(A_LATEST_VERSION_UUID))
          .thenReturn(Optional.empty());
      when(oisClient.getCourseByVersionExternalId(A_COURSE_UUID, A_LATEST_VERSION_UUID))
          .thenReturn(new OisCourseFullResponse(aClientCourseResponse(), aClientVersionResponse()));
      when(courseRepository.save(any(Course.class))).thenReturn(course);
      when(moduleRepository.findByTitleAndCurriculums_ExternalId(
              "Vabaained", aCurriculum().getExternalId()))
          .thenReturn(Optional.of(optionalSubjectsModule));
      when(moduleRepository.findByCourseId(course.getId()))
          .thenReturn(List.of(optionalSubjectsModule));

      plannedCourseService.updatePlannedCourses(
          A_STUDY_PLAN_EXTERNAL_ID, List.of(aPlannedCourseRequest()));

      verify(oisClient).getCourseByVersionExternalId(A_COURSE_UUID, A_LATEST_VERSION_UUID);
      verify(courseRepository).save(any(Course.class));
      verify(moduleRepository).save(optionalSubjectsModule);
      assertThat(optionalSubjectsModule.getCourses()).contains(course);
    }
  }

  @Test
  void resolveModule_courseHasModuleInCurriculum_doesNotFallBackToOptionalSubjects() {
    var course = aCourse();
    try (var mocked = mockStatic(UserRequestContext.class)) {
      mocked.when(UserRequestContext::getUserExternalId).thenReturn(A_USER_EXTERNAL_ID);

      when(studyPlanRepository.findByExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(Optional.of(aStudyPlan()));
      when(semesterRepository.findAllByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(List.of(aSemester()));
      when(courseRepository.findByCourseVersionExternalId(A_LATEST_VERSION_UUID))
          .thenReturn(Optional.of(course));
      when(moduleRepository.findByCourseId(course.getId())).thenReturn(List.of(aModule()));

      plannedCourseService.updatePlannedCourses(
          A_STUDY_PLAN_EXTERNAL_ID, List.of(aPlannedCourseRequest()));

      verify(moduleRepository, never()).findByTitleAndCurriculums_ExternalId(any(), any());
    }
  }

  @Test
  void resolveModule_courseHasNoModuleInCurriculum_fallsBackToOptionalSubjects() {
    var curriculum = aCurriculum();
    var course = aCourse();
    var moduleFromDifferentCurriculum = Module.builder().id(2L).curriculums(List.of()).build();
    var optionalSubjectsModule =
        Module.builder()
            .id(99L)
            .curriculums(List.of(curriculum))
            .courses(new ArrayList<>())
            .build();

    try (var mocked = mockStatic(UserRequestContext.class)) {
      mocked.when(UserRequestContext::getUserExternalId).thenReturn(A_USER_EXTERNAL_ID);

      when(studyPlanRepository.findByExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(Optional.of(aStudyPlan()));
      when(semesterRepository.findAllByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(List.of(aSemester()));
      when(courseRepository.findByCourseVersionExternalId(A_LATEST_VERSION_UUID))
          .thenReturn(Optional.of(course));
      when(moduleRepository.findByCourseId(course.getId()))
          .thenReturn(List.of(moduleFromDifferentCurriculum));
      when(moduleRepository.findByTitleAndCurriculums_ExternalId(
              "Vabaained", curriculum.getExternalId()))
          .thenReturn(Optional.of(optionalSubjectsModule));

      plannedCourseService.updatePlannedCourses(
          A_STUDY_PLAN_EXTERNAL_ID, List.of(aPlannedCourseRequest()));

      verify(moduleRepository)
          .findByTitleAndCurriculums_ExternalId("Vabaained", curriculum.getExternalId());
    }
  }
}
