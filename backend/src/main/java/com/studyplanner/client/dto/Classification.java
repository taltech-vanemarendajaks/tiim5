package com.studyplanner.client.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;

public record Classification(@NotNull @JsonProperty("study_level") OisStudyLevel oisStudyLevel) {}
