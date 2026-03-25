package com.studyplanner.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.studyplanner.exception.UUIDConversionException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UUIDUtilsTest {

  @Test
  void fromString_ShouldCorrectlyConvertStringToUUID() {
    String validUUIDString = "123e4567-e89b-12d3-a456-426614174000";

    UUID result = UUIDUtils.fromString(validUUIDString);

    assertAll(
        () -> assertNotNull(result), () -> assertEquals(UUID.fromString(validUUIDString), result));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "",
        "abc",
        "123e4567-",
        "77335f38-66a2-464a-a506-",
        "77335f38-66a2-464a-a506-ca7b1b2ac19bb"
      })
  void fromString_ShouldThrowExceptionForIncorrectUUIDs(String incorrectUUIDString) {
    assertThrows(UUIDConversionException.class, () -> UUIDUtils.fromString(incorrectUUIDString));
  }
}
