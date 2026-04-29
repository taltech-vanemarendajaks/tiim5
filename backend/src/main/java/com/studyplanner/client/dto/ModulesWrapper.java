package com.studyplanner.client.dto;

import jakarta.validation.constraints.*;
import java.util.*;

public record ModulesWrapper(@NotNull List<Block> blocks) {}
