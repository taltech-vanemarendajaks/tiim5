package com.studyplanner.controller;

import com.studyplanner.dto.CurriculumResponse;
import com.studyplanner.service.CurriculumService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Curriculum")
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class UserCurriculumController {

  private final CurriculumService curriculumService;

  @GetMapping("/study-plan/{studyPlanExternalId}/curriculum")
  public ResponseEntity<CurriculumResponse> getCurriculumByStudyPlan(
      @PathVariable UUID studyPlanExternalId) {
    return ResponseEntity.ok(curriculumService.getCurriculumByStudyPlan(studyPlanExternalId));
  }
}
