package com.studyplanner.dto;

import com.studyplanner.entity.SemesterType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CourseResponse(
    @NotNull UUID externalId,
    @NotBlank String titleEn,
    @NotBlank String titleEt,
    @NotBlank String code,
    @NotNull Double credits,
    @NotNull SemesterType semesterType) {}
