package com.studyplanner.service;

import com.studyplanner.entity.StudyPlan;
import com.studyplanner.exception.ResourceNotFoundException;
import com.studyplanner.repository.StudyPlanRepository;
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
}
