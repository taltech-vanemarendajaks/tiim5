package com.studyplanner.dto;

import com.studyplanner.entity.StudyLevel;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CurriculumResponse(
    @NotNull UUID externalId,
    @NotNull String title,
    @NotNull StudyLevel studyLevel,
    @NotNull Integer credits,
    @NotNull LocalDateTime creationDate) {}
