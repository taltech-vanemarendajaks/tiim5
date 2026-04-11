package com.studyplanner.mapper;

import com.studyplanner.dto.PlannedCourseRequest;
import com.studyplanner.dto.PlannedCourseResponse;
import com.studyplanner.entity.CourseStatus;
import com.studyplanner.entity.PlannedCourse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PlannedCourseMapper {

  public static PlannedCourseResponse mapToResponse(PlannedCourse course) {
    return PlannedCourseResponse.builder()
        .externalId(course.getExternalId())
        .course(CourseMapper.mapToResponse(course.getCourse()))
        .module(ModuleMapper.mapToResponse(course.getModule()))
        .courseStatus(course.getStatus())
        .build();
  }

  public static List<PlannedCourseResponse> mapToResponseList(List<PlannedCourse> courses) {
    return courses.stream().map(PlannedCourseMapper::mapToResponse).toList();
  }

  public static PlannedCourse mapToPlannedCourse(PlannedCourseRequest request) {
    return PlannedCourse.builder()
        .externalId(UUID.randomUUID())
        .status(request.status() != null ? request.status() : CourseStatus.PLANNED)
        .creationDate(LocalDateTime.now())
        .build();
  }
}
