package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

import com.studyplanner.client.OisClient;
import com.studyplanner.dto.CourseResponse;
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
    when(oisClient.getAllCourses(1, 300, "Kursus", null))
        .thenReturn(List.of(aClientCourseResponse()));
    when(oisClient.getAllCourseVersions(List.of(AN_LATEST_VERSION_UUID)))
        .thenReturn(Map.of(A_COURSE_UUID, List.of(aClientVersionResponse())));

    var actual = courseService.getAllCourses(1, 300, "Kursus", null);
    var first = actual.get(0);

    assertAll(
        "course response",
        () -> assertThat(first.titleEn()).isEqualTo(A_COURSE_TITLE_EN),
        () -> assertThat(first.titleEt()).isEqualTo(A_COURSE_TITLE_ET),
        () -> assertThat(first.credits()).isEqualTo(6.0),
        () -> assertThat(first.semesterType()).isEqualTo(SemesterType.SPRING));
  }

  @Test
  void getAllCourses_whenNoVersions_returnsCourseWithEmptySemesters() {
    when(oisClient.getAllCourses(1, 5, null, null)).thenReturn(List.of(aClientCourseResponse()));
    when(oisClient.getAllCourseVersions(List.of(AN_LATEST_VERSION_UUID))).thenReturn(Map.of());

    var result = courseService.getAllCourses(1, 5, null, null);

    assertThat(result.get(0).semesterType()).isNull();
  }
}
