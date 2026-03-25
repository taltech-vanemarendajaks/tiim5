package com.studyplanner.controller;

import com.studyplanner.dto.SemesterResponse;
import com.studyplanner.service.SemesterService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SemesterController {

  private final SemesterService semesterService;

  @GetMapping("/study-plans/{studyPlanExternalId}/semesters")
  public ResponseEntity<List<SemesterResponse>> getSemesters(
      @PathVariable UUID studyPlanExternalId) {
    return ResponseEntity.ok(semesterService.getSemesters(studyPlanExternalId));
  }
}
