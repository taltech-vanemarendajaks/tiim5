package com.studyplanner.client.dto;

import jakarta.validation.constraints.*;
import java.util.*;

public record Block(@NotNull List<OisModule> submodules) {}
