package com.studyplanner.mapper;

import com.studyplanner.client.dto.*;
import com.studyplanner.dto.CurriculumResponse;
import com.studyplanner.entity.*;
import java.util.*;

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

  public static CurriculumResponse mapOisToResponse(OisCurriculumResponse curriculum) {
    return CurriculumResponse.builder()
        .externalId(curriculum.externalId())
        .title(curriculum.title().et())
        .studyLevel(mapStudyLevel(curriculum.oisStudyLevel().code()))
        .credits(curriculum.credits())
        .build();
  }

  private static StudyLevel mapStudyLevel(String code) {
    return switch (code) {
      case "master" -> StudyLevel.MASTER;
      case "bachelor" -> StudyLevel.BACHELOR;
      case "integrated" -> StudyLevel.INTEGRATED;
      case "doctor" -> StudyLevel.DOCTOR;
      default -> null;
    };
  }

  public static Curriculum mapOisCurriculumVersionToCurriculum(
      OisCurriculumVersionResponse oisCurriculumVersionResponse) {
    return Curriculum.builder()
        .curriculumExternalId(null) // Must be set later
        .curriculumVersionExternalId(oisCurriculumVersionResponse.curriculumVersionId())
        .externalId(UUID.randomUUID())
        .title(oisCurriculumVersionResponse.title().et())
        .studyLevel(
            mapStudyLevel(oisCurriculumVersionResponse.classification().oisStudyLevel().code()))
        .credits(oisCurriculumVersionResponse.credits())
        .modules(new ArrayList<>())
        .build();
  }
}
