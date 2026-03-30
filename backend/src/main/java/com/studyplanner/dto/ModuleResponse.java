package com.studyplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ModuleResponse(
    @NotNull UUID externalId,
    @NotBlank String title,
    @NotNull Integer requiredCredits,
    Integer optionalCredits) {}
