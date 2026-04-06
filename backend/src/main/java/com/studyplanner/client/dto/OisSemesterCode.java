package com.studyplanner.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OisSemesterCode {
  SPRING("spring"),
  AUTUMN("autumn");

  private final String value;

  OisSemesterCode(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static OisSemesterCode fromValue(String value) {
    for (OisSemesterCode semester : values()) {
      if (semester.value.equalsIgnoreCase(value)) {
        return semester;
      }
    }
    return null;
  }
}
