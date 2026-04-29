package com.studyplanner.client.dto;

import com.fasterxml.jackson.annotation.*;
import java.util.*;

public record OisModule(
    Title title,
    @JsonProperty("min_credits") Double minCredits,
    @JsonProperty("max_credits") Double maxCredits,
    @JsonProperty("courses") List<OisModuleCourseResponse> OisModuleCourseResponse,
    @JsonProperty("submodules") List<OisModule> submodules,
    @JsonProperty("uuid") UUID moduleExternalId) {}
