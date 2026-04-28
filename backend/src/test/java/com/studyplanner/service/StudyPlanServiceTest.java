package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static com.studyplanner.common.UnitTestFixtures.A_STUDY_PLAN_EXTERNAL_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.studyplanner.entity.*;
import com.studyplanner.repository.StudyPlanRepository;
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
class StudyPlanServiceTest {

  @Mock private StudyPlanRepository studyPlanRepository;
  @Mock private SemesterService semesterService;
  @Mock private UserService userService;
  @Mock private CurriculumService curriculumService;
  @InjectMocks private StudyPlanService studyPlanService;

  @Test
  void fetchStudyPlanByIdTest() {
    var studyPlan = aStudyPlan();
    when(studyPlanRepository.findByExternalId(A_STUDY_PLAN_EXTERNAL_ID))
        .thenReturn(Optional.of(studyPlan));

    var actual = studyPlanService.fetchStudyPlanById(A_STUDY_PLAN_EXTERNAL_ID);

    assertEquals(studyPlan, actual);
  }

  @Test
  void getStudyPlansTest() {
    var studyPlans = List.of(aStudyPlan());
    var studyPlanResponse = List.of(aStudyPlanResponse());
    try (var mockedRequestContext = mockStatic(UserRequestContext.class)) {
      mockedRequestContext
          .when(UserRequestContext::getUserExternalId)
          .thenReturn(A_USER_EXTERNAL_ID);

      when(studyPlanRepository.findAllByUserExternalId(A_USER_EXTERNAL_ID)).thenReturn(studyPlans);

      var actual = studyPlanService.getStudyPlans();

      assertEquals(studyPlanResponse, actual);
    }
  }

  @Test
  void createNewSemesterForStudyPlan_addsSemesterAndSavesStudyPlan() {
    var studyPlan =
        StudyPlan.builder()
            .externalId(A_STUDY_PLAN_EXTERNAL_ID)
            .completedCredits(36)
            .user(aUser())
            .curriculum(aCurriculum())
            .creationDate(A_LOCAL_DATE_TIME)
            .semesters(new ArrayList<>())
            .build();
    var semester = aSemester();

    when(studyPlanRepository.findByExternalId(A_STUDY_PLAN_EXTERNAL_ID))
        .thenReturn(Optional.of(studyPlan));
    when(semesterService.createNewSemester(studyPlan, SemesterType.AUTUMN)).thenReturn(semester);
    when(studyPlanRepository.save(studyPlan)).thenReturn(studyPlan);

    var result =
        studyPlanService.createNewSemesterForStudyPlan(
            A_STUDY_PLAN_EXTERNAL_ID, SemesterType.AUTUMN);

    assertEquals(A_STUDY_PLAN_EXTERNAL_ID, result.externalId());
    verify(semesterService).createNewSemester(studyPlan, SemesterType.AUTUMN);
    verify(studyPlanRepository).save(studyPlan);
  }

  @Test
  void addNewStudyPlanForUserTest() {
    var studyPlan = aStudyPlan();
    var studyPlanResponse = aStudyPlanResponse();
    var user = aUser();
    var curriculum = aCurriculum();

    try (var mockedRequestContext = mockStatic(UserRequestContext.class)) {
      mockedRequestContext
          .when(UserRequestContext::getUserExternalId)
          .thenReturn(A_USER_EXTERNAL_ID);

      when(userService.getUserByExternalId(A_USER_EXTERNAL_ID)).thenReturn(user);
      when(curriculumService.initalizeCurriculum(any(), any())).thenReturn(curriculum);
      when(studyPlanRepository.save(any())).thenReturn(studyPlan);

      var actual = studyPlanService.addNewStudyPlanForUser(AN_EXTERNAL_ID, A_LATEST_VERSION_UUID);

      assertEquals(studyPlanResponse.externalId(), actual.externalId());
      verify(semesterService).createSemestersBasedOnCurriculum(any());
    }
  }
}
