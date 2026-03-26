package com.studyplanner.controller;

import com.studyplanner.dto.CourseVersionResponse;
import com.studyplanner.service.CourseVersionService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses/{courseUuid}/versions")
@RequiredArgsConstructor
public class CourseVersionController {

  private final CourseVersionService courseVersionService;

  @GetMapping
  public ResponseEntity<List<CourseVersionResponse>> getCourseVersions(
      @PathVariable UUID courseUuid) {
    return ResponseEntity.ok(courseVersionService.getCourseVersions(courseUuid));
  }
}
