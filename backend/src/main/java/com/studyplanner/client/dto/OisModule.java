package com.studyplanner.client.dto;

import com.fasterxml.jackson.annotation.*;
import java.util.*;

public record OisModule(
    Title title,
    @JsonProperty("min_credits") Integer minCredits,
    @JsonProperty("max_credits") Integer maxCredits,
    @JsonProperty("courses") List<OisModuleCourseResponse> OisModuleCourseResponse,
    @JsonProperty("submodules") List<OisModule> submodules,
    @JsonProperty("uuid") UUID moduleExternalId) {}
