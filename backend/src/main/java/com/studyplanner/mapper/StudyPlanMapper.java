package com.studyplanner.mapper;

import com.studyplanner.dto.StudyPlanResponse;
import com.studyplanner.entity.StudyPlan;
import java.util.List;

public class StudyPlanMapper {

  public static StudyPlanResponse mapToResponse(StudyPlan studyPlan) {
    return StudyPlanResponse.builder()
        .externalId(studyPlan.getExternalId())
        .name(studyPlan.getName())
        .completedCredits(studyPlan.getCompletedCredits())
        .startDate(studyPlan.getStartDate())
        .curriculum(CurriculumMapper.mapToResponse(studyPlan.getCurriculum()))
        .creationDate(studyPlan.getCreationDate())
        .build();
  }

  public static List<StudyPlanResponse> mapToResponseList(List<StudyPlan> studyPlans) {
    return studyPlans.stream().map(StudyPlanMapper::mapToResponse).toList();
  }
}
