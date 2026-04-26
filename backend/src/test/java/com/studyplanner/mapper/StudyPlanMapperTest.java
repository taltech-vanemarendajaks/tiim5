package com.studyplanner.mapper;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class StudyPlanMapperTest {
  @Test
  void mapToResponseTest() {
    var studyplan = aStudyPlan();
    var studyPlanResponse = aStudyPlanResponse();

    var actual = StudyPlanMapper.mapToResponse(studyplan);

    assertEquals(studyPlanResponse, actual);
  }

  @Test
  void mapToResponseListTest() {
    var studyPlan = List.of(aStudyPlan());
    var studyPlanResponse = List.of(aStudyPlanResponse());

    var actual = StudyPlanMapper.mapToResponseList(studyPlan);

    assertEquals(studyPlanResponse, actual);
  }
}
