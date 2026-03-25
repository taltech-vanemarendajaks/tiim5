package com.studyplanner.mapper;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class PlannedCourseMapperTest {
  @Test
  void mapToResponseTest() {
    var plannedCourse = aPlannedCourse();
    var plannedCourseResponse = aPlannedCourseResponse();

    var actual = PlannedCourseMapper.mapToResponse(plannedCourse);

    assertEquals(plannedCourseResponse, actual);
  }

  @Test
  void mapToResponseListTest() {
    var plannedCourse = List.of(aPlannedCourse());
    var plannedCourseResponse = List.of(aPlannedCourseResponse());

    var actual = PlannedCourseMapper.mapToResponseList(plannedCourse);

    assertEquals(plannedCourseResponse, actual);
  }
}
