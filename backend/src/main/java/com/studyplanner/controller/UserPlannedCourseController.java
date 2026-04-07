package com.studyplanner.controller;

import com.studyplanner.dto.PlannedCourseRequest;
import com.studyplanner.dto.PlannedCourseResponse;
import com.studyplanner.service.PlannedCourseService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/planned-courses")
@RequiredArgsConstructor
public class UserPlannedCourseController {

  private final PlannedCourseService plannedCourseService;

  @PutMapping("/study-plan/{studyPlanExternalId}")
  public ResponseEntity<List<PlannedCourseResponse>> updatePlannedCourses(
      @PathVariable UUID studyPlanExternalId,
      @RequestBody @Valid List<PlannedCourseRequest> request) {
    return ResponseEntity.ok(
        plannedCourseService.updatePlannedCourses(studyPlanExternalId, request));
  }
}
