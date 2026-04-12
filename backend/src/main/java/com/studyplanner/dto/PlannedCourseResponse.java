package com.studyplanner.dto;

import com.studyplanner.entity.CourseStatus;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record PlannedCourseResponse(
    @NotNull UUID externalId,
    @NotNull CourseResponse course,
    @NotNull ModuleResponse module,
    @NotNull CourseStatus courseStatus) {}
