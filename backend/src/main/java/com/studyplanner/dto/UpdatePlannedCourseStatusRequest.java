package com.studyplanner.dto;

import com.studyplanner.entity.CourseStatus;
import jakarta.validation.constraints.NotNull;

public record UpdatePlannedCourseStatusRequest(@NotNull CourseStatus status) {}
