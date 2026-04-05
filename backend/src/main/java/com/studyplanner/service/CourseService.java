package com.studyplanner.service;

import com.studyplanner.client.OisClient;
import com.studyplanner.client.dto.OisCourseResponse;
import com.studyplanner.client.dto.OisVersionResponse;
import com.studyplanner.dto.CourseResponse;
import com.studyplanner.mapper.CourseMapper;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

  private final OisClient oisClient;

  public List<CourseResponse> getAllCourses(int start, int take, String title, String code) {
    List<OisCourseResponse> oisCourses = oisClient.getAllCourses(start, take, title, code);
    Map<UUID, List<OisVersionResponse>> versionsByCourseUuid = getVersionsByCourseUuid(oisCourses);

    return oisCourses.stream()
        .map(course -> mapToCourseResponse(course, versionsByCourseUuid))
        .toList();
  }

  private Map<UUID, List<OisVersionResponse>> getVersionsByCourseUuid(
      List<OisCourseResponse> oisCourses) {
    List<UUID> versionUuids = extractVersionUuids(oisCourses);

    if (versionUuids.isEmpty()) {
      return Map.of();
    }

    return oisClient.getAllCourseVersions(versionUuids);
  }

  private List<UUID> extractVersionUuids(List<OisCourseResponse> oisCourses) {
    return oisCourses.stream()
        .map(OisCourseResponse::latestVersion)
        .filter(Objects::nonNull)
        .distinct()
        .toList();
  }

  private CourseResponse mapToCourseResponse(
      OisCourseResponse course, Map<UUID, List<OisVersionResponse>> versionsByCourseUuid) {

    List<OisVersionResponse> versions =
        versionsByCourseUuid.getOrDefault(course.externalId(), List.of());

    OisVersionResponse firstVersion = versions.isEmpty() ? null : versions.get(0);

    return CourseMapper.mapOisToResponse(course, firstVersion);
  }
}
