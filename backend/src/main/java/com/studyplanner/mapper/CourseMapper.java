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
        .semesterType(mapSemesterType(version.target()))
        .versionExternalId(version == null ? null : version.uuid())
        .build();
  }

  public static SemesterType mapSemesterType(OisVersionResponse.Target target) {
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
