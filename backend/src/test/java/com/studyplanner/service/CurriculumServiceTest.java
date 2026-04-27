package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.studyplanner.repository.CurriculumRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurriculumServiceTest {

  @Mock private CurriculumRepository curriculumRepository;
  @InjectMocks private CurriculumService curriculumService;

  @Test
  void getCurriculumTest() {
    var curriculum = aCurriculum();
    var curriculumResponse = aCurriculumResponse();

    when(curriculumRepository.findByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID))
        .thenReturn(Optional.ofNullable(curriculum));

    var actual = curriculumService.getCurriculumByStudyPlan(A_STUDY_PLAN_EXTERNAL_ID);

    assertEquals(curriculumResponse, actual);
  }
}
