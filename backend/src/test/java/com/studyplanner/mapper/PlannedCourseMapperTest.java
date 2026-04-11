package com.studyplanner.mapper;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.studyplanner.entity.CourseStatus;
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

  @Test
  void mapToPlannedCourseTest() {
    var request = aPlannedCourseRequest();

    var actual = PlannedCourseMapper.mapToPlannedCourse(request);

    assertThat(actual.getStatus()).isEqualTo(CourseStatus.PLANNED);
  }
}
