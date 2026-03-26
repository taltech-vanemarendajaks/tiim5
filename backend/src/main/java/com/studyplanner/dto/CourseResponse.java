package com.studyplanner.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.studyplanner.entity.SemesterType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record CourseResponse(
    @JsonProperty("uuid") UUID externalId,
    @NotBlank String code,
    @NotBlank Title title,
    @NotNull Double credits,
    @NotNull List<SemesterType> semesters) {

  public record Title(String en, String et) {}
}
