package com.studyplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record SemesterResponse(
    @NotNull UUID externalId,
    @NotBlank String name,
    @NotNull Boolean finished,
    List<PlannedCourseResponse> plannedCourses,
    @NotNull LocalDateTime creationDate) {}
