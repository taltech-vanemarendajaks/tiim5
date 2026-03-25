package com.studyplanner.exception;

public class UUIDConversionException extends RuntimeException {
  public UUIDConversionException(final String uuidString) {
    super("Could not convert string to UUID: " + uuidString);
  }
}
