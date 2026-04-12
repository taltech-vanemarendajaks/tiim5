package com.studyplanner.mapper;

import com.studyplanner.client.dto.*;
import com.studyplanner.dto.CourseResponse;
import com.studyplanner.entity.Course;
import com.studyplanner.entity.SemesterType;
import java.time.LocalDateTime;
import java.util.UUID;

public class CourseMapper {

  public static CourseResponse mapToResponse(Course course) {
    return CourseResponse.builder()
        .externalId(course.getExternalId())
        .oisExternalId(course.getCourseExternalId())
        .versionExternalId(course.getCourseVersionExternalId())
        .titleEn(course.getTitleEn())
        .titleEt(course.getTitleEt())
        .code(course.getCode())
        .semesterType(course.getSemesterType())
        .credits(course.getCredits())
        .build();
  }

  public static CourseResponse mapOisToResponse(
      OisCourseResponse course, OisVersionResponse version) {

    return CourseResponse.builder()
        .oisExternalId(course.externalId())
        .code(course.code())
        .titleEn(course.title().en())
        .titleEt(course.title().et())
        .credits(course.credits())
        .semesterType(version != null ? mapSemesterType(version.target()) : null)
        .versionExternalId(version == null ? null : version.uuid())
        .build();
  }

  public static Course mapToCourse(OisCourseFullResponse response) {
    return Course.builder()
        .externalId(UUID.randomUUID())
        .courseExternalId(response.externalId())
        .courseVersionExternalId(response.latestVersion())
        .titleEn(response.title().en())
        .titleEt(response.title().et())
        .code(response.code())
        .credits(response.credits())
        .semesterType(mapSemesterType(response.target()))
        .creationDate(LocalDateTime.now())
        .build();
  }

  private static SemesterType mapSemesterType(OisTargetResponse target) {
    if (target == null || target.semester() == null) {
      return null;
    }

    OisSemesterCode code = target.semester().code();

    return switch (code) {
      case AUTUMN -> SemesterType.AUTUMN;
      case SPRING -> SemesterType.SPRING;
    };
  }
}
