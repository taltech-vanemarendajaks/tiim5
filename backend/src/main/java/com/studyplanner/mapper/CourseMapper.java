package com.studyplanner.mapper;

import com.studyplanner.dto.CourseResponse;
import com.studyplanner.entity.Course;

public class CourseMapper {

  public static CourseResponse mapToResponse(Course course) {
    return CourseResponse.builder()
        .externalId(course.getExternalId())
        .title(course.getTitle())
        .code(course.getCode())
        .semesterType(course.getSemesterType())
        .credits(course.getCredits())
        .build();
  }
}
