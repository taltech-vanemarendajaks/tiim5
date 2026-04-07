package com.studyplanner.client.dto;

import lombok.Builder;

@Builder
public record OisCourseFullResponse(
    OisCourseResponse courseResponse, OisVersionResponse versionResponse) {}
