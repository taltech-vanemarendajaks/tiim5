package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.studyplanner.repository.ModuleRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ModuleServiceTest {

  @Mock private ModuleRepository moduleRepository;
  @InjectMocks private ModuleService moduleService;

  @Test
  void fetchModuleByTitleAndCurriculumExternalIdTest() {
    var module = aModule();
    when(moduleRepository.findByTitleAndCurriculums_ExternalId(A_TITLE_EN, AN_EXTERNAL_ID))
        .thenReturn(Optional.of(module));

    var actual =
        moduleService.fetchModuleByTitleAndCurriculumExternalId(A_TITLE_EN, AN_EXTERNAL_ID);

    assertEquals(module, actual);
  }

  @Test
  void fetchModulesByCourseIdsTest() {
    var modules = List.of(aModule());
    when(moduleRepository.findModulesWithCurriculums(List.of(1L))).thenReturn(modules);

    var actual = moduleService.fetchModulesByCourseIds(List.of(1L));

    assertEquals(modules, actual);
  }
}
