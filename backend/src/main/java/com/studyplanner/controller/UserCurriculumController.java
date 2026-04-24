package com.studyplanner.controller;

import com.studyplanner.dto.CurriculumResponse;
import com.studyplanner.service.CurriculumService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Curriculum")
@RequestMapping("/api/v1/users/me/curriculum")
@RequiredArgsConstructor
public class UserCurriculumController {

  private final CurriculumService curriculumService;

  @GetMapping
  public ResponseEntity<CurriculumResponse> getCurriculum() {
    return ResponseEntity.ok(curriculumService.getCurriculum());
  }
}
