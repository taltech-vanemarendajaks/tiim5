package com.studyplanner.mapper;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class SemesterMapperTest {
  @Test
  void mapToResponseTest() {
    var semester = aSemester();
    var semesterResponse = aSemesterResponse();

    var actual = SemesterMapper.mapToResponse(semester);

    assertEquals(semesterResponse, actual);
  }

  @Test
  void mapToResponseListTest() {
    var semester = List.of(aSemester());
    var semesterResponse = List.of(aSemesterResponse());

    var actual = SemesterMapper.mapToResponseList(semester);

    assertEquals(semesterResponse, actual);
  }
}
