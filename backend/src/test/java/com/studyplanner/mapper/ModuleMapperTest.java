package com.studyplanner.mapper;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class ModuleMapperTest {
  @Test
  void mapToResponseTest() {
    var module = aModule();
    var moduleResponse = aModuleResponse();

    var actual = ModuleMapper.mapToResponse(module);

    assertEquals(moduleResponse, actual);
  }

  @Test
  void mapToResponseListTest() {
    var module = List.of(aModule());
    var moduleResponse = List.of(aModuleResponse());

    var actual = ModuleMapper.mapToResponseList(module);

    assertEquals(moduleResponse, actual);
  }
}
