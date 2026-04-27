package com.studyplanner.mapper;

import com.studyplanner.dto.ModuleResponse;
import com.studyplanner.entity.Module;
import java.util.List;

public class ModuleMapper {

  public static ModuleResponse mapToResponse(Module module) {
    return ModuleResponse.builder()
        .externalId(module.getExternalId())
        .title(module.getTitle())
        .requiredCredits(module.getRequiredCredits())
        .optionalCredits(module.getOptionalCredits())
        .build();
  }

  public static List<ModuleResponse> mapToResponseList(List<Module> modules) {
    return modules.stream().map(ModuleMapper::mapToResponse).toList();
  }
}
