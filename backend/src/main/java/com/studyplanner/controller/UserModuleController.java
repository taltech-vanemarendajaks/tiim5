package com.studyplanner.controller;

import com.studyplanner.dto.ModuleResponse;
import com.studyplanner.service.ModuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Module")
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class UserModuleController {

  private final ModuleService moduleService;

  @GetMapping("/study-plan/{studyPlanExternalId}/modules")
  public ResponseEntity<List<ModuleResponse>> getModules(@PathVariable UUID studyPlanExternalId) {
    return ResponseEntity.ok(moduleService.getModules(studyPlanExternalId));
  }
}
