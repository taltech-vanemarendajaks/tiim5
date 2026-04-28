package com.studyplanner.service;

import com.studyplanner.dto.*;
import com.studyplanner.entity.*;
import com.studyplanner.exception.ResourceNotFoundException;
import com.studyplanner.mapper.*;
import com.studyplanner.repository.*;
import com.studyplanner.utils.UserRequestContext;
import java.time.*;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyPlanService {

  private final StudyPlanRepository studyPlanRepository;
  private final SemesterService semesterService;
  private final UserService userService;
  private final CurriculumService curriculumService;

  public StudyPlan fetchStudyPlanById(UUID studyPlanExternalId) {
    return studyPlanRepository
        .findByExternalId(studyPlanExternalId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "No study plan found for external id " + studyPlanExternalId));
  }

  public List<StudyPlanResponse> getStudyPlans() {
    UUID userExternalId = UserRequestContext.getUserExternalId();
    return StudyPlanMapper.mapToResponseList(
        studyPlanRepository.findAllByUserExternalId(userExternalId));
  }

  public StudyPlan saveNewStudyPlanForUser(User user, Curriculum curriculum) {

    StudyPlan studyPlan = new StudyPlan();
    studyPlan.setName(curriculum.getTitle());
    studyPlan.setCurriculum(curriculum);
    studyPlan.setCompletedCredits(0);
    studyPlan.setUser(user);
    studyPlan.setStartDate(LocalDateTime.now());
    studyPlan.setExternalId(UUID.randomUUID());

    StudyPlan savedStudyPlan = studyPlanRepository.save(studyPlan);

    semesterService.createSemestersBasedOnCurriculum(savedStudyPlan);

    return savedStudyPlan;
  }

  public StudyPlanResponse addNewStudyPlanForUser(UUID curriculumId, UUID curriculumVersionId) {
    UUID userExternalId = UserRequestContext.getUserExternalId();
    User user = userService.getUserByExternalId(userExternalId);
    Curriculum curriculum =
        curriculumService.initalizeCurriculum(curriculumId, curriculumVersionId);

    return StudyPlanMapper.mapToResponse(this.saveNewStudyPlanForUser(user, curriculum));
  }

  public StudyPlanResponse createNewSemesterForStudyPlan(
      UUID studyPlanExternalId, SemesterType semesterType) {
    StudyPlan studyPlan = this.fetchStudyPlanById(studyPlanExternalId);

    Semester semester = semesterService.createNewSemester(studyPlan, semesterType);
    studyPlan.getSemesters().add(semester);

    StudyPlan savedStudyPlan = studyPlanRepository.save(studyPlan);
    return StudyPlanMapper.mapToResponse(savedStudyPlan);
  }
}
