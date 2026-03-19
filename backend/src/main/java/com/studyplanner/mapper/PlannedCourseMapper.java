package com.studyplanner.mapper;

import com.studyplanner.dto.PlannedCourseResponse;
import com.studyplanner.entity.PlannedCourse;
import java.util.List;

public class PlannedCourseMapper {

  public static PlannedCourseResponse mapToResponse(PlannedCourse course) {
    return PlannedCourseResponse.builder()
        .externalId(course.getExternalId())
        .course(CourseMapper.mapToResponse(course.getCourse()))
        .build();
  }

  public static List<PlannedCourseResponse> mapToResponseList(List<PlannedCourse> courses) {
    return courses.stream().map(PlannedCourseMapper::mapToResponse).toList();
  }
}
