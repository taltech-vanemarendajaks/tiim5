package com.studyplanner.client;

import com.studyplanner.client.dto.CourseResponse;
import com.studyplanner.client.dto.CourseVersionResponse;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ApiClient {

  private final WebClient webClient;

  public ApiClient(WebClient webClient) {
    this.webClient = webClient;
  }

  public List<CourseResponse> getAllCourses() {
    return webClient
        .get()
        .uri("/courses")
        .retrieve()
        .bodyToFlux(CourseResponse.class)
        .collectList()
        .block();
  }

  public List<CourseVersionResponse> getCourseVersions(UUID courseUuid) {
    return webClient
        .get()
        .uri("/courses/{courseKey}/versions", courseUuid)
        .retrieve()
        .bodyToFlux(CourseVersionResponse.class)
        .collectList()
        .block();
  }
}
