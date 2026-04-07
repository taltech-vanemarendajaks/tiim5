package com.studyplanner.mapper;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ModuleMapperTest {
  @Test
  void mapToResponseTest() {
    var module = aModule();
    var moduleResponse = aModuleResponse();

    var actual = ModuleMapper.mapToResponse(module);

    assertEquals(moduleResponse, actual);
  }
}
