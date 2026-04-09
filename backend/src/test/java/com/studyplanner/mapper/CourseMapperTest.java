package com.studyplanner.mapper;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.studyplanner.entity.SemesterType;
import org.junit.jupiter.api.Test;

class CourseMapperTest {
  @Test
  void mapToResponseTest() {
    var course = aCourse();
    var courseResponse = aCourseResponse();

    var actual = CourseMapper.mapToResponse(course);

    assertEquals(courseResponse, actual);
  }

  @Test
  void mapClientToResponseTest() {
    var clientCourse = aClientCourseResponse();
    var clientVersion = aClientVersionResponse();

    var actual = CourseMapper.mapOisToResponse(clientCourse, clientVersion);

    assertAll(
        "course mapping",
        () -> assertThat(actual.oisExternalId()).isEqualTo(A_COURSE_UUID),
        () -> assertThat(actual.code()).isEqualTo("1"),
        () -> assertThat(actual.titleEn()).isEqualTo(A_TITLE_EN),
        () -> assertThat(actual.titleEt()).isEqualTo(A_TITLE_ET),
        () -> assertThat(actual.credits()).isEqualTo(6.0),
        () -> assertThat(actual.semesterType()).isEqualTo(SemesterType.SPRING));
  }

  @Test
  void mapToCourseTest() {
    var oisResponse = aOisCourseFullResponse();

    var actual = CourseMapper.mapToCourse(oisResponse);

    assertAll(
        () -> assertThat(actual.getCourseVersionExternalId()).isEqualTo(A_LATEST_VERSION_UUID),
        () -> assertThat(actual.getCode()).isEqualTo("123"),
        () -> assertThat(actual.getTitleEn()).isEqualTo(A_TITLE_EN),
        () -> assertThat(actual.getTitleEt()).isEqualTo(A_TITLE_ET),
        () -> assertThat(actual.getSemesterType()).isEqualTo(SemesterType.SPRING));
  }
}
