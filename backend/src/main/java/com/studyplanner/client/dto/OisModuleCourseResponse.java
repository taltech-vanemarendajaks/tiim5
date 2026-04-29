package com.studyplanner.client.dto;

import com.fasterxml.jackson.annotation.*;
import java.util.*;

public record OisModuleCourseResponse(
    @JsonProperty("main_uuid") UUID externalId, @JsonProperty("is_required") Boolean isRequired) {}
