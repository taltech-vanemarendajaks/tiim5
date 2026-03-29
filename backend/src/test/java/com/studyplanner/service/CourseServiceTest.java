package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.studyplanner.client.OisClient;
import com.studyplanner.entity.SemesterType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

  @Mock private OisClient oisClient;
  @InjectMocks private CourseService courseService;

  @Test
  void getAllCoursesTest() {
    when(oisClient.getAllCourses()).thenReturn(List.of(aClientCourseResponse()));
    when(oisClient.getAllCourseVersions(List.of(AN_LATEST_VERSION_UUID)))
        .thenReturn(Map.of(A_COURSE_UUID, List.of(aClientVersionResponse())));

    var actual = courseService.getAllCourses();

    assertThat(actual).hasSize(1);
    assertThat(actual.get(0).code()).isEqualTo("1");
    assertThat(actual.get(0).semesterType()).isEqualTo(SemesterType.SPRING);
  }

  @Test
  void getAllCourses_whenNoVersions_returnsCourseWithEmptySemesters() {
    when(oisClient.getAllCourses()).thenReturn(List.of(aClientCourseResponse()));
    when(oisClient.getAllCourseVersions(List.of(AN_LATEST_VERSION_UUID))).thenReturn(Map.of());

    var result = courseService.getAllCourses();

    assertThat(result.get(0).semesterType()).isNull();
  }
}
