package com.studyplanner.controller;

import com.studyplanner.dto.*;
import com.studyplanner.entity.*;
import com.studyplanner.service.StudyPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Study plan")
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class UserStudyPlanController {

  private final StudyPlanService studyPlanService;

  @GetMapping("/study-plans")
  public ResponseEntity<List<StudyPlanResponse>> getStudyPlans() {
    return ResponseEntity.ok(studyPlanService.getStudyPlans());
  }

  @PostMapping("/study-plan/{studyPlanExternalId}/semesters/{semesterType}")
  public ResponseEntity<StudyPlanResponse> addSemester(
      @PathVariable UUID studyPlanExternalId, @PathVariable SemesterType semesterType) {
    return ResponseEntity.ok(
        studyPlanService.createNewSemesterForStudyPlan(studyPlanExternalId, semesterType));
  }

  @PostMapping("/study-plans/new")
  public ResponseEntity<StudyPlanResponse> addNewStudyPlan(
      @RequestBody CreateNewStudyPlanRequest createNewStudyPlanRequest) {
    System.out.println(createNewStudyPlanRequest);
    return ResponseEntity.ok(
        studyPlanService.addNewStudyPlanForAuthenticatedUser(
            createNewStudyPlanRequest.curriculumId(),
            createNewStudyPlanRequest.curriculumVersionId()));
  }
}
