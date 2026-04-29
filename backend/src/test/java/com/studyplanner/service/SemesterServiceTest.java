package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.studyplanner.dto.CurriculumResponse;
import com.studyplanner.entity.*;
import com.studyplanner.repository.SemesterRepository;
import com.studyplanner.utils.UserRequestContext;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SemesterServiceTest {

  @Mock private SemesterRepository semesterRepository;
  @Mock private CurriculumService curriculumService;
  @InjectMocks private SemesterService semesterService;

  @Test
  void getSemestersTest() {
    var semester = List.of(aSemester());
    var semesterResponse = List.of(aSemesterResponse());
    try (var mockedRequestContext = mockStatic(UserRequestContext.class)) {
      mockedRequestContext
          .when(UserRequestContext::getUserExternalId)
          .thenReturn(A_USER_EXTERNAL_ID);

      when(semesterRepository.findAllByUserAndStudyPlanExternalId(
              A_USER_EXTERNAL_ID, A_STUDY_PLAN_EXTERNAL_ID))
          .thenReturn(semester);

      var actual = semesterService.getSemesters(A_STUDY_PLAN_EXTERNAL_ID);

      assertEquals(semesterResponse, actual);
    }
  }

  @Test
  void fetchSemestersByStudyPlanExternalIdTest() {
    var semesters = List.of(aSemester());
    when(semesterRepository.findAllByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID))
        .thenReturn(semesters);

    var actual = semesterService.fetchSemestersByStudyPlanExternalId(A_STUDY_PLAN_EXTERNAL_ID);

    assertEquals(semesters, actual);
  }

  @Test
  void createNewSemester_savesAndReturnsSemester() {
    var studyPlan = aStudyPlan();
    when(semesterRepository.save(any(Semester.class))).thenAnswer(inv -> inv.getArgument(0));

    var result = semesterService.createNewSemester(studyPlan, SemesterType.AUTUMN);

    assertEquals(SemesterType.AUTUMN, result.getSemesterType());
    assertEquals(studyPlan, result.getStudyPlan());
    verify(semesterRepository).save(any(Semester.class));
  }

  @Test
  void createSemestersBasedOnCurriculum_forBachelor_createsSixSemesters() {
    var studyPlan = aStudyPlan();
    when(curriculumService.getCurriculumByStudyPlan(A_STUDY_PLAN_EXTERNAL_ID))
        .thenReturn(aCurriculumResponse());
    when(semesterRepository.save(any(Semester.class))).thenAnswer(inv -> inv.getArgument(0));

    var result = semesterService.createSemestersBasedOnCurriculum(studyPlan);

    assertEquals(6, result.size());
    verify(semesterRepository, times(6)).save(any(Semester.class));
  }

  @Test
  void createSemestersBasedOnCurriculum_forMaster_createsFourSemesters() {
    var studyPlan = aStudyPlan();
    var masterCurriculumResponse =
        CurriculumResponse.builder()
            .externalId(AN_EXTERNAL_ID)
            .title(A_TITLE_EN)
            .studyLevel(StudyLevel.MASTER)
            .credits(120)
            .creationDate(A_LOCAL_DATE_TIME)
            .build();
    when(curriculumService.getCurriculumByStudyPlan(A_STUDY_PLAN_EXTERNAL_ID))
        .thenReturn(masterCurriculumResponse);
    when(semesterRepository.save(any(Semester.class))).thenAnswer(inv -> inv.getArgument(0));

    var result = semesterService.createSemestersBasedOnCurriculum(studyPlan);

    assertEquals(4, result.size());
  }

  @Test
  void createSemestersBasedOnCurriculum_alternatesSemesterTypes() {
    var studyPlan = aStudyPlan();
    when(curriculumService.getCurriculumByStudyPlan(A_STUDY_PLAN_EXTERNAL_ID))
        .thenReturn(aCurriculumResponse());
    when(semesterRepository.save(any(Semester.class))).thenAnswer(inv -> inv.getArgument(0));

    var result = semesterService.createSemestersBasedOnCurriculum(studyPlan);

    assertEquals(SemesterType.AUTUMN, result.get(0).getSemesterType());
    assertEquals(SemesterType.SPRING, result.get(1).getSemesterType());
    assertEquals(SemesterType.AUTUMN, result.get(2).getSemesterType());
  }
}
