package com.studyplanner.controller;

import com.studyplanner.client.dto.CourseResponse;
import com.studyplanner.service.CourseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

  private final CourseService courseService;

  @GetMapping
  public ResponseEntity<List<CourseResponse>> getAllCourses() {
    return ResponseEntity.ok(courseService.getAllCourses());
  }
}
