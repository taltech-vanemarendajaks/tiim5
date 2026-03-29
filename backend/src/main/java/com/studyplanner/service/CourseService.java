package com.studyplanner.service;

import com.studyplanner.client.OisClient;
import com.studyplanner.client.dto.ClientCourseResponse;
import com.studyplanner.client.dto.ClientVersionResponse;
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

  public List<CourseResponse> getAllCourses() {
    List<ClientCourseResponse> clientCourses = oisClient.getAllCourses();

    List<UUID> versionUuids =
        clientCourses.stream()
            .map(ClientCourseResponse::latestVersion)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

    Map<UUID, List<ClientVersionResponse>> versionsByCourseUuid =
        versionUuids.isEmpty() ? Map.of() : oisClient.getAllCourseVersions(versionUuids);

    return clientCourses.stream()
        .map(
            course -> {
              List<ClientVersionResponse> versions =
                  versionsByCourseUuid.getOrDefault(course.externalId(), List.of());

              ClientVersionResponse firstVersion = versions.isEmpty() ? null : versions.get(0);

              return CourseMapper.mapClientToResponse(course, firstVersion);
            })
        .toList();
  }
}
