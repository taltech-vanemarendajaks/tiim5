package com.studyplanner.exception;

public class AccessDeniedException extends RuntimeException {
  public AccessDeniedException(final String userExternalId) {
    super("Access to resource denied for user : " + userExternalId);
  }
}
