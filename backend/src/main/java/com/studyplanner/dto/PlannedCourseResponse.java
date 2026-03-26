package com.studyplanner.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

import com.studyplanner.client.dto.CourseResponse;

import lombok.Builder;

@Builder
public record PlannedCourseResponse(@NotNull UUID externalId, @NotNull CourseResponse course) {}
