package com.studyplanner.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserResponse(@NotEmpty UUID externalId, @NotEmpty String name) {}
