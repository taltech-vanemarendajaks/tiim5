package com.studyplanner.dto;

import jakarta.validation.constraints.*;
import java.util.*;
import lombok.*;

@Builder
public record CurriculumVersionResponse(@NotNull UUID externalVersionId, @NotNull int year) {}
