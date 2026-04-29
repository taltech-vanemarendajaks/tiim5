package com.studyplanner.service;

import com.studyplanner.dto.ModuleResponse;
import com.studyplanner.entity.Module;
import com.studyplanner.exception.ResourceNotFoundException;
import com.studyplanner.mapper.ModuleMapper;
import com.studyplanner.repository.ModuleRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModuleService {

  private final ModuleRepository moduleRepository;

  public List<Module> fetchModulesByCourseIds(List<Long> courseIds) {
    return moduleRepository.findModulesWithCurriculums(courseIds);
  }

  public Module fetchModuleByTitleAndCurriculumExternalId(String title, UUID curriculumExternalId) {
    return moduleRepository
        .findByTitleAndCurriculums_ExternalId(title, curriculumExternalId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format(
                        "Module with title: %s doesn't exist for curriculum: %s",
                        title, curriculumExternalId)));
  }

  public List<ModuleResponse> getModules(UUID studyPlanExternalId) {
    return ModuleMapper.mapToResponseList(
        moduleRepository.findModulesByStudyPlanExternalId(studyPlanExternalId));
  }

  public Module saveModule(Module module) {
    return moduleRepository.save(module);
  }
}
