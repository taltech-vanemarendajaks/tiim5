package com.studyplanner.client.dto;

import java.util.List;
import java.util.UUID;

public record ClientCourseVersionRequest(List<UUID> uuids) {}
