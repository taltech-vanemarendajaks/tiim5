package com.studyplanner.dto;

import jakarta.validation.constraints.*;
import java.util.*;
import lombok.*;

@Builder
public record RegisterUserRequest(
    @NotNull String name,
    @NotNull String studyLevel,
    UUID curriculumVersionId,
    UUID curriculumId) {}
