package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.studyplanner.client.OisClient;
import com.studyplanner.entity.SemesterType;
import com.studyplanner.repository.CourseRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

  @Mock private OisClient oisClient;
  @Mock private CourseRepository courseRepository;
  @InjectMocks private CourseService courseService;

  @Test
  void getAllCoursesTest() {
    when(oisClient.getAllCourses(1, 300, "Kursus", null))
        .thenReturn(List.of(aClientCourseResponse()));
    when(oisClient.getAllCourseVersions(List.of(A_LATEST_VERSION_UUID)))
        .thenReturn(Map.of(A_COURSE_UUID, List.of(aClientVersionResponse())));

    var actual = courseService.getAllCourses(1, 300, "Kursus", null);
    var first = actual.getFirst();

    assertAll(
        "course response",
        () -> assertThat(first.titleEn()).isEqualTo(A_TITLE_EN),
        () -> assertThat(first.titleEt()).isEqualTo(A_TITLE_ET),
        () -> assertThat(first.credits()).isEqualTo(6.0),
        () -> assertThat(first.semesterType()).isEqualTo(SemesterType.SPRING));
  }

  @Test
  void getAllCourses_whenNoVersions_returnsCourseWithEmptySemesters() {
    when(oisClient.getAllCourses(1, 5, null, null)).thenReturn(List.of(aClientCourseResponse()));
    when(oisClient.getAllCourseVersions(List.of(A_LATEST_VERSION_UUID))).thenReturn(Map.of());

    var result = courseService.getAllCourses(1, 5, null, null);

    assertThat(result.getFirst().semesterType()).isNull();
  }

  @Test
  void fetchCoursesByVersionExternalIdsTest() {
    var courses = List.of(aCourse());
    when(courseRepository.findAllByCourseVersionExternalIdIn(List.of(A_LATEST_VERSION_UUID)))
        .thenReturn(courses);

    var actual = courseService.fetchCoursesByVersionExternalIds(List.of(A_LATEST_VERSION_UUID));

    assertEquals(courses, actual);
  }

  @Test
  void getFromOisAndSaveCourse_whenCourseIsNew_savesAndReturnsIt() {
    var course = aCourse();

    when(oisClient.getCourseByVersionExternalId(A_COURSE_UUID, A_LATEST_VERSION_UUID))
        .thenReturn(aOisCourseFullResponse());
    when(courseRepository.findByCourseVersionExternalId(A_LATEST_VERSION_UUID))
        .thenReturn(Optional.empty());
    when(courseRepository.save(any())).thenReturn(course);

    var actual = courseService.getFromOisAndSaveCourse(A_COURSE_UUID, A_LATEST_VERSION_UUID);

    assertEquals(Optional.of(course), actual);
    verify(courseRepository).save(any());
  }

  @Test
  void getFromOisAndSaveCourse_whenCourseAlreadyExists_returnsExistingWithoutSaving() {
    var existingCourse = aCourse();

    when(oisClient.getCourseByVersionExternalId(A_COURSE_UUID, A_LATEST_VERSION_UUID))
        .thenReturn(aOisCourseFullResponse());
    when(courseRepository.findByCourseVersionExternalId(A_LATEST_VERSION_UUID))
        .thenReturn(Optional.of(existingCourse));

    var actual = courseService.getFromOisAndSaveCourse(A_COURSE_UUID, A_LATEST_VERSION_UUID);

    assertEquals(Optional.of(existingCourse), actual);
    verify(courseRepository, never()).save(any());
  }
}
