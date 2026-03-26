package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.studyplanner.client.ApiClient;
import com.studyplanner.client.dto.CourseResponse;
import com.studyplanner.entity.SemesterType;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

  @Mock private ApiClient apiClient;
  @InjectMocks private CourseService courseService;

  @Test
  void getAllCoursesTest() {
    var courseResponse = List.of(aCourseResponse());

    when(apiClient.getAllCourses()).thenReturn(courseResponse);
    when(apiClient.getCourseVersions(AN_EXTERNAL_ID)).thenReturn(List.of(aCourseVersionResponse()));

    List<CourseResponse> actual = courseService.getAllCourses();

    assertThat(actual).hasSize(1);
    assertThat(actual.get(0).code()).isEqualTo("1");
    assertThat(actual.get(0).semesters()).containsExactly(SemesterType.SPRING);
  }

  @Test
  void getAllCourses_whenNoVersions_returnsCourseWithEmptySemesters() {
    when(apiClient.getAllCourses()).thenReturn(List.of(aCourseResponse()));
    when(apiClient.getCourseVersions(AN_EXTERNAL_ID)).thenReturn(List.of());

    List<CourseResponse> result = courseService.getAllCourses();

    assertThat(result.get(0).semesters()).isEmpty();
  }
}
