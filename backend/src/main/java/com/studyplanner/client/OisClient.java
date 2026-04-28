package com.studyplanner.client;

import com.studyplanner.client.dto.*;
import com.studyplanner.dto.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OisClient {

  private static final String COURSES_URI = "/courses";
  private static final String COURSES_VERSIONS_DETAILS_URI = "/courses/versions/details";
  private static final String COURSE_DETAILS_BY_VERSION_URI =
      "/courses/{externalId}/versions/{versionExternalId}";
  private static final String CURRICULUM_URI = "/curricula";
  private static final String CURRICULUM_BY_VERSION_URI =
      "/curricula/curricula-version/{curriculumVersionId}";
  private final RestClient restClient;
  private final String CURRICULUM_VERSIONS_BY_ID = "/curricula/{curriculumId}/versions";

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
        .body(new ParameterizedTypeReference<>() {});
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

  public OisCourseFullResponse getCourseByVersionExternalId(
      UUID externalId, UUID versionExternalId) {
    return restClient
        .get()
        .uri(COURSE_DETAILS_BY_VERSION_URI, externalId, versionExternalId)
        .retrieve()
        .body(OisCourseFullResponse.class);
  }

  public List<OisCurriculumVersionPartialResponse> getAllCurriculumVersions(String curriculumId) {
    return restClient
        .get()
        .uri(CURRICULUM_VERSIONS_BY_ID, curriculumId)
        .retrieve()
        .body(new ParameterizedTypeReference<>() {});
  }

  public List<OisCurriculumResponse> getAllCurriculums(
      int start, int take, String q, String study_level) {
    OisCurriculumRequest request = new OisCurriculumRequest(start, take, q, study_level);
    return restClient
        .post()
        .uri(CURRICULUM_URI)
        .body(request)
        .retrieve()
        .body(new ParameterizedTypeReference<>() {});
  }

  public OisCurriculumVersionResponse getCurriculumVersionById(String curriculumVersionId) {
    return restClient
        .get()
        .uri(CURRICULUM_BY_VERSION_URI, curriculumVersionId)
        .retrieve()
        .body(new ParameterizedTypeReference<>() {});
  }

  public List<OisCourseResponse> getCoursesBatched(List<UUID> courseUuids) {
    return restClient
        .post()
        .uri(COURSES_URI)
        .body(Map.of("uuids", courseUuids))
        .retrieve()
        .body(new ParameterizedTypeReference<>() {});
  }
}
