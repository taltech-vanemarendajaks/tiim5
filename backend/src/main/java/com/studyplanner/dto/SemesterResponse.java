package com.studyplanner.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record SemesterResponse(
    @NotNull UUID externalId,
    @NotNull Integer year,
    @NotNull Boolean finished,
    List<PlannedCourseResponse> plannedCourses,
    @NotNull LocalDateTime creationDate) {}
