package com.studyplanner.controller;

import com.studyplanner.dto.CourseResponse;
import com.studyplanner.service.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Course")
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

  private final CourseService courseService;

  @GetMapping
  public ResponseEntity<List<CourseResponse>> getAllCourses(
      @RequestParam(defaultValue = "1") int start,
      @RequestParam(defaultValue = "300") int take,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String code) {
    return ResponseEntity.ok(courseService.getAllCourses(start, take, title, code));
  }
}
