package com.studyplanner.client;

import com.studyplanner.client.dto.ClientCourseResponse;
import com.studyplanner.client.dto.ClientCourseVersionRequest;
import com.studyplanner.client.dto.ClientVersionResponse;
import com.studyplanner.client.dto.CourseVersionsResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OisClient {

  private final RestClient restClient;

  public OisClient(@Value("${ois.base-url}") String baseUrl) {
    this.restClient =
        RestClient.builder().baseUrl(baseUrl).defaultHeader("Accept", "application/json").build();
  }

  public List<ClientCourseResponse> getAllCourses() {
    return restClient.get().uri("/courses").retrieve().body(new ParameterizedTypeReference<>() {});
  }

  public Map<UUID, List<ClientVersionResponse>> getAllCourseVersions(List<UUID> uuids) {
    CourseVersionsResponse response =
        restClient
            .post()
            .uri("/courses/versions/details")
            .body(new ClientCourseVersionRequest(uuids))
            .retrieve()
            .body(CourseVersionsResponse.class);

    return response != null && response.coursesVersions() != null
        ? response.coursesVersions()
        : Map.of();
  }
}
