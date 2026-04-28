package com.studyplanner.mapper;

import com.fasterxml.jackson.annotation.*;
import com.studyplanner.client.dto.*;
import com.studyplanner.dto.ModuleResponse;
import com.studyplanner.entity.Module;
import java.util.*;

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

  public static Module OisModuleToModule(OisModule oisModule) {
    return Module.builder()
        .externalId(UUID.randomUUID())
        .moduleExternalId(oisModule.moduleExternalId())
        .title(oisModule.title().et())
        .courses(new ArrayList<>())
        .requiredCredits(oisModule.minCredits())
        .optionalCredits(0)
        .build();
  }
}
