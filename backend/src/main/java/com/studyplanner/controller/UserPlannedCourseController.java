package com.studyplanner.controller;

import com.studyplanner.dto.PlannedCourseRequest;
import com.studyplanner.dto.PlannedCourseResponse;
import com.studyplanner.service.PlannedCourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Planned course")
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class UserPlannedCourseController {

  private final PlannedCourseService plannedCourseService;

  @PutMapping("/study-plan/{studyPlanExternalId}/planned-courses")
  public ResponseEntity<List<PlannedCourseResponse>> setPlannedCourses(
      @PathVariable UUID studyPlanExternalId,
      @RequestBody @Valid List<PlannedCourseRequest> request) {
    return ResponseEntity.ok(plannedCourseService.setPlannedCourses(studyPlanExternalId, request));
  }
}
