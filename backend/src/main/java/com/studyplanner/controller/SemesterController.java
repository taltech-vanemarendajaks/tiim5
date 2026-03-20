package com.studyplanner.controller;

import com.studyplanner.dto.SemesterResponse;
import com.studyplanner.service.SemesterService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/semesters")
@RequiredArgsConstructor
public class SemesterController {

  private final SemesterService semesterService;

  @GetMapping("/{userExternalId}/study-plan/{studyPlanExternalId}")
  public ResponseEntity<List<SemesterResponse>> getUserSemesters(
      @PathVariable UUID userExternalId, @PathVariable UUID studyPlanExternalId) {
    return ResponseEntity.ok(semesterService.getUserSemesters(userExternalId, studyPlanExternalId));
  }
}
