package com.studyplanner.service;

import com.studyplanner.dto.StudyPlanResponse;
import com.studyplanner.entity.PlannedCourse;
import com.studyplanner.entity.StudyPlan;
import com.studyplanner.exception.ResourceNotFoundException;
import com.studyplanner.mapper.StudyPlanMapper;
import com.studyplanner.repository.StudyPlanRepository;
import com.studyplanner.utils.UserRequestContext;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public void recalculateAndSave(StudyPlan studyPlan, Collection<PlannedCourse> plannedCourses) {
    studyPlan.recalculateCompletedCredits(plannedCourses);
    studyPlanRepository.save(studyPlan);
  }
}
