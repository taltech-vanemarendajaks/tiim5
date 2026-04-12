package com.studyplanner.mapper;

import com.studyplanner.dto.ModuleResponse;
import com.studyplanner.entity.Module;

public class ModuleMapper {

  public static ModuleResponse mapToResponse(Module module) {
    return ModuleResponse.builder()
        .externalId(module.getExternalId())
        .title(module.getTitle())
        .requiredCredits(module.getRequiredCredits())
        .optionalCredits(module.getOptionalCredits())
        .build();
  }
}
