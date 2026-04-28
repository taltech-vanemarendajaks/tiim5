package com.studyplanner.controller;

import com.studyplanner.dto.*;
import com.studyplanner.dto.CurriculumResponse;
import com.studyplanner.entity.*;
import com.studyplanner.service.*;
import com.studyplanner.service.CurriculumService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @GetMapping("/curriculums")
  public ResponseEntity<List<CurriculumResponse>> getAllCurriculums(
      @RequestParam(defaultValue = "1") int start,
      @RequestParam(defaultValue = "24") int take,
      @RequestParam(required = false) String q,
      @RequestParam(value = "study_level", required = false, defaultValue = "bachelor")
          String study_level) {

    return ResponseEntity.ok(curriculumService.getAllCurriculums(start, take, q, study_level));
  }

  @GetMapping("/curriculums/{curriculumId}/versions")
  public ResponseEntity<List<CurriculumVersionResponse>> getVersionsForCurriculum(
      @PathVariable String curriculumId) {
    return ResponseEntity.ok(curriculumService.getVersionsForCurriculum(curriculumId));
  }

  @PostMapping("/curriculums/new")
  public ResponseEntity<CurriculumResponse> saveNewCurriculum(
      UUID curriculumId, UUID curriculumVersionId) {
    return ResponseEntity.ok(curriculumService.addNewCurriculum(curriculumId, curriculumVersionId));
  }
}
