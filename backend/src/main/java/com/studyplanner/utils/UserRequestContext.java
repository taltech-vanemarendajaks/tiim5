package com.studyplanner.utils;

import com.studyplanner.exception.RequestAttributesException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserRequestContext {

  public static final String USER_EXTERNAL_ID_HEADER = "User-External-Id";

  public static UUID getUserExternalId() {
    HttpServletRequest request = getHttpServletRequest();
    String value = request.getHeader(USER_EXTERNAL_ID_HEADER);

    if (value == null || value.isBlank()) {
      throw new RequestAttributesException("Missing User-External-Id header");
    }

    return UUIDUtils.fromString(value);
  }

  private static HttpServletRequest getHttpServletRequest() {
    return ((ServletRequestAttributes) getRequestAttributes()).getRequest();
  }

  private static RequestAttributes getRequestAttributes() {
    RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    if (attributes == null) {
      throw new RequestAttributesException("Request attributes not found");
    }

    return attributes;
  }
}
