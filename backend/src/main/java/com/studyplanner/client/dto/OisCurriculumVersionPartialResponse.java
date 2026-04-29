package com.studyplanner.client.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OisCurriculumVersionPartialResponse(
    @NotNull @JsonProperty("uuid") UUID curriculumVersionId,
    @NotNull @JsonProperty("year") int versionYear) {}
