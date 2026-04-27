package com.studyplanner.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record StudyPlanResponse(
    @NotNull UUID externalId,
    String name,
    @NotNull Integer completedCredits,
    @NotNull LocalDateTime startDate,
    @NotNull CurriculumResponse curriculum,
    @NotNull LocalDateTime creationDate) {}
