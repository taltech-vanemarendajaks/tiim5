package com.studyplanner.service;

import com.studyplanner.dto.StudyPlanResponse;
import com.studyplanner.entity.CourseStatus;
import com.studyplanner.entity.StudyPlan;
import com.studyplanner.exception.ResourceNotFoundException;
import com.studyplanner.mapper.StudyPlanMapper;
import com.studyplanner.repository.StudyPlanRepository;
import com.studyplanner.utils.UserRequestContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyPlanService {

  private final StudyPlanRepository studyPlanRepository;

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

  @Transactional
  public void recalculateAndSaveCompletedCredits(UUID studyPlanExternalId) {
    StudyPlan studyPlan = fetchStudyPlanById(studyPlanExternalId);

    double completedCredits =
        studyPlan.getSemesters().stream()
            .flatMap(semester -> semester.getPlannedCourses().stream())
            .filter(pc -> pc.getStatus() == CourseStatus.COMPLETED)
            .mapToDouble(pc -> pc.getCourse().getCredits())
            .sum();

    studyPlan.setCompletedCredits(completedCredits);
    studyPlan.setUpdateDate(LocalDateTime.now());
    studyPlanRepository.save(studyPlan);
  }
}
