package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static com.studyplanner.common.UnitTestFixtures.A_STUDY_PLAN_EXTERNAL_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.studyplanner.repository.StudyPlanRepository;
import com.studyplanner.utils.UserRequestContext;
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
}
