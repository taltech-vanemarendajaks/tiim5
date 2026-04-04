package com.studyplanner.client;

import com.studyplanner.client.dto.OisCourseResponse;
import com.studyplanner.client.dto.OisCourseVersionsResponse;
import com.studyplanner.client.dto.OisVersionResponse;
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
  private static final String COURSES_URI = "/courses";
  private static final String COURSES_VERSIONS_DETAILS_URI = "/courses/versions/details";

  public OisClient(@Value("${ois.base-url}") String baseUrl) {
    this.restClient =
        RestClient.builder().baseUrl(baseUrl).defaultHeader("Accept", "application/json").build();
  }

  public List<OisCourseResponse> getAllCourses(int start, int take, String title, String code) {
    return restClient
        .get()
        .uri(
            uriBuilder -> {
              uriBuilder.path(COURSES_URI).queryParam("start", start).queryParam("take", take);

              if (title != null && !title.isBlank()) {
                uriBuilder.queryParam("title", title);
              }

              if (code != null && !code.isBlank()) {
                uriBuilder.queryParam("code", code);
              }

              return uriBuilder.build();
            })
        .retrieve()
        .body(new ParameterizedTypeReference<List<OisCourseResponse>>() {});
  }

  public Map<UUID, List<OisVersionResponse>> getAllCourseVersions(List<UUID> uuids) {
    OisCourseVersionsResponse response =
        restClient
            .post()
            .uri(COURSES_VERSIONS_DETAILS_URI)
            .body(Map.of("uuids", uuids))
            .retrieve()
            .body(OisCourseVersionsResponse.class);

    return response != null && response.coursesVersions() != null
        ? response.coursesVersions()
        : Map.of();
  }
}
