package com.studyplanner.service;

import com.studyplanner.client.ApiClient;
import com.studyplanner.client.dto.CourseVersionResponse;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseVersionService {

  private final ApiClient apiClient;

  public List<CourseVersionResponse> getCourseVersions(UUID courseVersion) {
    return apiClient.getCourseVersions(courseVersion);
  }
}
