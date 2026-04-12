package com.studyplanner.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OisTargetResponse(Semester semester) {
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Semester(OisSemesterCode code, String et, String en) {}
}
