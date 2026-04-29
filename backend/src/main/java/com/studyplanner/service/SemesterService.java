package com.studyplanner.service;

import com.studyplanner.dto.SemesterResponse;
import com.studyplanner.entity.*;
import com.studyplanner.mapper.SemesterMapper;
import com.studyplanner.repository.SemesterRepository;
import com.studyplanner.utils.UserRequestContext;
import jakarta.transaction.Transactional;
import java.time.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemesterService {

  private final SemesterRepository semesterRepository;
  private final CurriculumService curriculumService;

  private final Integer SEMESTERS_COUNT_FOR_MASTERS = 4;
  private final Integer SEMESTERS_COUNT_FOR_BACHELORS = 6;
  private final Integer SEMESTERS_COUNT_FOR_INTEGRATED = 6;
  private final Integer SEMESTERS_COUNT_DEFAULT = 4;

  public List<SemesterResponse> getSemesters(UUID studyPlanExternalId) {
    UUID userExternalId = UserRequestContext.getUserExternalId();
    return SemesterMapper.mapToResponseList(
        semesterRepository.findAllByUserAndStudyPlanExternalId(
            userExternalId, studyPlanExternalId));
  }

  public List<Semester> fetchSemestersByStudyPlanExternalId(UUID studyPlanExternalId) {
    return semesterRepository.findAllByStudyPlanExternalId(studyPlanExternalId);
  }

  @Transactional
  public void recalculateAndSave(Semester semester) {
    semester.recalculateFinished();
    semesterRepository.save(semester);
  }

  public Semester createNewSemester(StudyPlan studyPlan, SemesterType semesterType) {

    Semester semester = new Semester();
    semester.setFinished(false);
    semester.setExternalId(UUID.randomUUID());
    int year = LocalDate.now().getYear();
    semester.setSemesterType(semesterType);
    semester.setYear(year);
    semester.setStudyPlan(studyPlan);

    return semesterRepository.save(semester);
  }

  public List<Semester> createSemestersBasedOnCurriculum(StudyPlan studyPlan) {
    Integer semesterCount;
    StudyLevel studyLevel =
        curriculumService.getCurriculumByStudyPlan(studyPlan.getExternalId()).studyLevel();

    switch (studyLevel) {
      case MASTER -> semesterCount = SEMESTERS_COUNT_FOR_MASTERS;
      case BACHELOR -> semesterCount = SEMESTERS_COUNT_FOR_BACHELORS;
      case INTEGRATED -> semesterCount = SEMESTERS_COUNT_FOR_INTEGRATED;
      default -> semesterCount = SEMESTERS_COUNT_DEFAULT;
    }

    return IntStream.range(0, semesterCount)
        .mapToObj(
            i -> {
              Semester semester = new Semester();
              semester.setFinished(false);
              semester.setExternalId(UUID.randomUUID());

              // TODO save curriculum metadata for start time
              int year = LocalDate.now().getYear() + (i + 1) / 2;

              if (i % 2 == 0) {
                semester.setSemesterType(SemesterType.AUTUMN);
              } else {
                semester.setSemesterType(SemesterType.SPRING);
              }

              semester.setYear(year);
              semester.setStudyPlan(studyPlan);

              return semesterRepository.save(semester);
            })
        .collect(Collectors.toList());
  }
}
