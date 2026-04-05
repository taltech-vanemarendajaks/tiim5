package com.studyplanner.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record OisCourseResponse(
    @JsonProperty("uuid") UUID externalId,
    @NotBlank String code,
    @NotBlank Title title,
    @NotNull Double credits,
    @JsonProperty("latest_version_uuid") UUID latestVersion) {}
