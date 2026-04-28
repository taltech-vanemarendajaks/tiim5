package com.studyplanner.client.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import java.util.*;

public record OisCurriculumResponse(
    @JsonProperty("uuid") UUID externalId,
    @NotBlank Title title,
    @NotNull Integer credits,
    @NotNull @JsonProperty("study_level") OisStudyLevel oisStudyLevel) {}
