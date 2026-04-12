package com.studyplanner.utils;

import com.studyplanner.exception.UUIDConversionException;
import java.util.UUID;

public class UUIDUtils {
  private UUIDUtils() {}

  public static UUID fromString(String uuidString) {
    try {
      if (uuidString == null) {
        return null;
      }
      return UUID.fromString(uuidString);
    } catch (Exception _) {
      throw new UUIDConversionException(uuidString);
    }
  }
}
