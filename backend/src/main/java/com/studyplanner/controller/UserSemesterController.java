package com.studyplanner.controller;

import com.studyplanner.dto.SemesterResponse;
import com.studyplanner.entity.*;
import com.studyplanner.service.SemesterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Semester")
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class UserSemesterController {

  private final SemesterService semesterService;

  @GetMapping("/study-plan/{studyPlanExternalId}/semesters")
  public ResponseEntity<List<SemesterResponse>> getSemesters(
      @PathVariable UUID studyPlanExternalId) {
    return ResponseEntity.ok(semesterService.getSemesters(studyPlanExternalId));
  }
}
