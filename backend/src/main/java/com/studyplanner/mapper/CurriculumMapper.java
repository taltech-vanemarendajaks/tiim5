package com.studyplanner.mapper;

import com.studyplanner.dto.CurriculumResponse;
import com.studyplanner.entity.Curriculum;

public class CurriculumMapper {

  public static CurriculumResponse mapToResponse(Curriculum curriculum) {
    return CurriculumResponse.builder()
        .externalId(curriculum.getExternalId())
        .title(curriculum.getTitle())
        .studyLevel(curriculum.getStudyLevel())
        .credits(curriculum.getCredits())
        .creationDate(curriculum.getCreationDate())
        .build();
  }
}
