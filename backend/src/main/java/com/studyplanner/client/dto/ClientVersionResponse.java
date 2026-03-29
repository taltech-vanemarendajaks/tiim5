package com.studyplanner.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ClientVersionResponse(@NotBlank UUID uuid, @NotBlank Target target) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Target(Semester semester) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Semester(String code, String et, String en) {}
  }
}
