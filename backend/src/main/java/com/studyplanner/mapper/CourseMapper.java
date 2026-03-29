package com.studyplanner.mapper;

import com.studyplanner.client.dto.ClientCourseResponse;
import com.studyplanner.client.dto.ClientVersionResponse;
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

  public static CourseResponse mapClientToResponse(
      ClientCourseResponse course, ClientVersionResponse version) {

    return CourseResponse.builder()
        .externalId(course.externalId())
        .code(course.code())
        .titleEn(course.title().en())
        .titleEt(course.title().et())
        .credits(course.credits())
        .semesterType(mapSemesterType(version))
        .build();
  }

  private static SemesterType mapSemesterType(ClientVersionResponse version) {
    if (version == null || version.target() == null || version.target().semester() == null) {
      return null;
    }

    String code = version.target().semester().code();

    return switch (code) {
      case "autumn" -> SemesterType.AUTUMN;
      case "spring" -> SemesterType.SPRING;
      default -> null;
    };
  }
}
