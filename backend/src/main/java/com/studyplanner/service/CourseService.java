package com.studyplanner.service;

import com.studyplanner.client.ApiClient;
import com.studyplanner.dto.CourseResponse;
import com.studyplanner.entity.SemesterType;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

  private final ApiClient apiClient;

  public List<CourseResponse> getAllCourses() {
    return apiClient.getAllCourses().stream()
        .map(
            course -> {
              List<SemesterType> semesters =
                  apiClient.getCourseVersions(course.externalId()).stream()
                      .map(v -> v.target().semester().code())
                      .distinct()
                      .map(
                          code ->
                              switch (code) {
                                case "autumn" -> SemesterType.AUTUMN;
                                case "spring" -> SemesterType.SPRING;
                                default -> null;
                              })
                      .filter(Objects::nonNull)
                      .toList();

              return new CourseResponse(
                  course.externalId(), course.code(), course.title(), course.credits(), semesters);
            })
        .toList();
  }
}
