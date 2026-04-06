package com.studyplanner.dto;

import com.studyplanner.entity.CourseStatus;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record PlannedCourseRequest(
    @NotNull UUID semesterExternalId,
    @NotNull UUID courseVersionExternalId,
    @NotNull String courseCode,
    CourseStatus status) {}
