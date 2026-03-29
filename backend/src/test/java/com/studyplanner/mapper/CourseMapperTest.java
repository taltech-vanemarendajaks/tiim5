package com.studyplanner.mapper;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
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

    var actual = CourseMapper.mapClientToResponse(clientCourse, clientVersion);

    assertThat(actual.externalId()).isEqualTo(AN_EXTERNAL_ID);
    assertThat(actual.code()).isEqualTo("1");
    assertThat(actual.titleEn()).isEqualTo("Course");
    assertThat(actual.titleEt()).isEqualTo("Kursus");
    assertThat(actual.credits()).isEqualTo(6.0);
    assertThat(actual.semesterType()).isEqualTo(SemesterType.SPRING);
  }
}
