package com.studyplanner.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OisCourseVersionsResponse(
    @JsonProperty("courses_versions") Map<UUID, List<OisVersionResponse>> coursesVersions) {}
