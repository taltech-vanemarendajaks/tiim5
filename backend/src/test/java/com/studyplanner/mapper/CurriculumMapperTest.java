package com.studyplanner.mapper;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CurriculumMapperTest {
  @Test
  void mapToResponseTest() {
    var curriculum = aCurriculum();
    var curriculumResponse = aCurriculumResponse();

    var actual = CurriculumMapper.mapToResponse(curriculum);

    assertEquals(curriculumResponse, actual);
  }
}
