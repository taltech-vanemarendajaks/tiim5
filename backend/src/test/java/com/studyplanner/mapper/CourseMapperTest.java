package com.studyplanner.mapper;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CourseMapperTest {
  @Test
  void mapToResponseTest() {
    var course = aCourse();
    var courseResponse = aCourseResponse();

    var actual = CourseMapper.mapToResponse(course);

    assertEquals(courseResponse, actual);
  }
}
