package com.studyplanner.mapper;

import com.studyplanner.client.dto.OisCourseResponse;
import com.studyplanner.client.dto.OisSemesterCode;
import com.studyplanner.client.dto.OisVersionResponse;
import com.studyplanner.dto.CourseResponse;
import com.studyplanner.entity.Course;
import com.studyplanner.entity.SemesterType;

public class CourseMapper {

  public static CourseResponse mapToResponse(Course course) {
    return CourseResponse.builder()
        .externalId(course.getExternalId())
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
        .externalId(course.externalId())
        .code(course.code())
        .titleEn(course.title().en())
        .titleEt(course.title().et())
        .credits(course.credits())
        .semesterType(mapSemesterType(version))
        .versionExternalId(version == null ? null : version.uuid())
        .build();
  }

  private static SemesterType mapSemesterType(OisVersionResponse version) {
    if (version == null || version.target() == null || version.target().semester() == null) {
      return null;
    }

    OisSemesterCode code = version.target().semester().code();

    return switch (code) {
      case AUTUMN -> SemesterType.AUTUMN;
      case SPRING -> SemesterType.SPRING;
    };
  }
}
