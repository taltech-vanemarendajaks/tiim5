package com.studyplanner.controller;

import com.studyplanner.dto.StudyPlanResponse;
import com.studyplanner.service.StudyPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
