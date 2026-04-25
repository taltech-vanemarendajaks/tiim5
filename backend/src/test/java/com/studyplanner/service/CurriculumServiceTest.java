package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static com.studyplanner.common.UnitTestFixtures.A_USER_EXTERNAL_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.studyplanner.repository.CurriculumRepository;
import com.studyplanner.utils.UserRequestContext;
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
    try (var mockedRequestContext = mockStatic(UserRequestContext.class)) {
      mockedRequestContext
          .when(UserRequestContext::getUserExternalId)
          .thenReturn(A_USER_EXTERNAL_ID);

      when(curriculumRepository.findByUserExternalId(A_USER_EXTERNAL_ID)).thenReturn(curriculum);

      var actual = curriculumService.getCurriculum();

      assertEquals(curriculumResponse, actual);
    }
  }
}
