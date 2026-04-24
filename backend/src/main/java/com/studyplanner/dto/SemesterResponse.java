package com.studyplanner.dto;

import com.studyplanner.entity.SemesterType;
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
    SemesterType semesterType,
    List<PlannedCourseResponse> plannedCourses,
    @NotNull LocalDateTime creationDate) {}
