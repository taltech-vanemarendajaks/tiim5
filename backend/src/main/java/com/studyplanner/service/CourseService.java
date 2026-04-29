package com.studyplanner.service;

import com.studyplanner.client.OisClient;
import com.studyplanner.client.dto.OisCourseFullResponse;
import com.studyplanner.client.dto.OisCourseResponse;
import com.studyplanner.client.dto.OisVersionResponse;
import com.studyplanner.dto.CourseResponse;
import com.studyplanner.entity.Course;
import com.studyplanner.mapper.CourseMapper;
import com.studyplanner.repository.CourseRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

  private final OisClient oisClient;
  private final CourseRepository courseRepository;

  public List<CourseResponse> getAllCourses(int start, int take, String title, String code) {
    List<OisCourseResponse> oisCourses = oisClient.getAllCourses(start, take, title, code);
    Map<UUID, List<OisVersionResponse>> versionsByCourseUuid = getVersionsByCourseUuid(oisCourses);

    return oisCourses.stream()
        .map(course -> mapToCourseResponse(course, versionsByCourseUuid))
        .toList();
  }

  public List<Course> fetchCoursesByVersionExternalIds(List<UUID> courseVersionExternalIds) {
    return courseRepository.findAllByCourseVersionExternalIdIn(courseVersionExternalIds);
  }

  public Optional<Course> getFromOisAndSaveCourse(
      UUID courseExternalId, UUID courseVersionExternalId) {
    OisCourseFullResponse response =
        oisClient.getCourseByVersionExternalId(courseExternalId, courseVersionExternalId);

    if (response.credits() == null) {
      System.out.println("Skipping course  — OIS returned null credits");
      return Optional.empty();
    }

    Course course = CourseMapper.mapToCourse(response);
    return Optional.of(
        courseRepository
            .findByCourseVersionExternalId(course.getCourseVersionExternalId())
            .orElseGet(() -> courseRepository.save(course)));
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

    OisVersionResponse firstVersion = versions.isEmpty() ? null : versions.getFirst();

    return CourseMapper.mapOisToResponse(course, firstVersion);
  }
}
