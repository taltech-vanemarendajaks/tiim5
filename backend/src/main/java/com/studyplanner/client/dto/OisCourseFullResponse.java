package com.studyplanner.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record OisCourseFullResponse(
    @JsonProperty("parent_uuid") UUID externalId,
    @JsonProperty("parent_code") String code,
    Title title,
    Double credits,
    @JsonProperty("uuid") UUID latestVersion,
    OisVersionResponse.Target target) {}
