package com.studyplanner.mapper;

import com.studyplanner.dto.SemesterResponse;
import com.studyplanner.entity.Semester;
import java.util.List;

public class SemesterMapper {

  public static SemesterResponse mapToResponse(Semester semester) {
    return SemesterResponse.builder()
        .externalId(semester.getExternalId())
        .year(semester.getYear())
        .finished(semester.getFinished())
        .semesterType(semester.getSemesterType())
        .plannedCourses(PlannedCourseMapper.mapToResponseList(semester.getPlannedCourses()))
        .creationDate(semester.getCreationDate())
        .build();
  }

  public static List<SemesterResponse> mapToResponseList(List<Semester> semesters) {
    return semesters.stream().map(SemesterMapper::mapToResponse).toList();
  }
}
