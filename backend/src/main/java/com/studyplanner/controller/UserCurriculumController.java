package com.studyplanner.controller;

import com.studyplanner.dto.*;
import com.studyplanner.dto.CurriculumResponse;
import com.studyplanner.entity.*;
import com.studyplanner.service.*;
import com.studyplanner.service.CurriculumService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Curriculum")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserCurriculumController {

  private final CurriculumService curriculumService;

  @GetMapping("/users/me/study-plan/{studyPlanExternalId}/curriculum")
  public ResponseEntity<CurriculumResponse> getCurriculumByStudyPlan(
      @PathVariable UUID studyPlanExternalId) {
    return ResponseEntity.ok(curriculumService.getCurriculumByStudyPlan(studyPlanExternalId));
  }
}
