package com.studyplanner.client.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import java.util.*;

public record OisCurriculumVersionResponse(
    @JsonProperty("uuid") UUID curriculumVersionId,
    @NotBlank Title title,
    @NotNull Integer credits,
    @NotNull @JsonProperty("classification") Classification classification,
    @JsonProperty("modules") ModulesWrapper modules) {}
