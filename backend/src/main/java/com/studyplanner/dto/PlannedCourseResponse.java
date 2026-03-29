package com.studyplanner.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record PlannedCourseResponse(@NotNull UUID externalId, @NotNull CourseResponse course) {}
