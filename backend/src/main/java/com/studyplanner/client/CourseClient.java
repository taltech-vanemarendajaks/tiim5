package com.studyplanner.client;

import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.studyplanner.client.dto.CourseResponse;

@Component
@RequiredArgsConstructor
public class CourseClient {

  private final RestClient restClient;

  public List<CourseResponse> getAllCourses() {
    return restClient
        .get()
        .uri("/courses")
        .retrieve()
         .body(new ParameterizedTypeReference<>() {});
  }
}
