package com.studyplanner.controller;

import com.studyplanner.dto.*;
import com.studyplanner.dto.CurriculumResponse;
import com.studyplanner.entity.*;
import com.studyplanner.service.*;
import com.studyplanner.service.CurriculumService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Curriculum")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CurriculumController {

  private final CurriculumService curriculumService;

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
}
