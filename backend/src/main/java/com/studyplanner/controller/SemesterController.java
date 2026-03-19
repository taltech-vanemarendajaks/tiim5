package com.studyplanner.controller;

import com.studyplanner.dto.SemesterResponse;
import com.studyplanner.service.SemesterService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/semesters")
@RequiredArgsConstructor
public class SemesterController {

  private final SemesterService semesterService;

  @GetMapping("/{userExternalId}")
  public ResponseEntity<List<SemesterResponse>> getUserSemesters(
      @PathVariable UUID userExternalId) {
    return ResponseEntity.ok(semesterService.getUserSemesters(userExternalId));
  }
}
