package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.studyplanner.repository.SemesterRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SemesterServiceTest {

  @Mock private SemesterRepository semesterRepository;
  @InjectMocks private SemesterService semesterService;

  @Test
  void getUserSemestersTest() {
    var semester = List.of(aSemester());
    var semesterResponse = List.of(aSemesterResponse());

    when(semesterRepository.findAllByUserAndStudyPlanExternalId(
            A_USER_EXTERNAL_ID, A_STUDY_PLAN_EXTERNAL_ID))
        .thenReturn(semester);

    var actual = semesterService.getUserSemesters(A_USER_EXTERNAL_ID, A_STUDY_PLAN_EXTERNAL_ID);

    assertEquals(semesterResponse, actual);
  }
}
