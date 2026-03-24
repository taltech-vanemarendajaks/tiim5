package com.studyplanner.utils;

import static com.studyplanner.common.UnitTestFixtures.A_USER_EXTERNAL_ID;
import static com.studyplanner.utils.UserRequestContext.USER_EXTERNAL_ID_HEADER;
import static org.junit.jupiter.api.Assertions.*;

import com.studyplanner.exception.RequestAttributesException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class UserRequestContextTest {

  private final MockHttpServletRequest mockRequest = new MockHttpServletRequest();

  @AfterEach
  void tearDown() {
    RequestContextHolder.resetRequestAttributes();
  }

  @Test
  void getUserExternalIdFromRequestContext_shouldReadValueFromHeader() {
    mockRequest.addHeader(USER_EXTERNAL_ID_HEADER, A_USER_EXTERNAL_ID.toString());
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

    assertEquals(A_USER_EXTERNAL_ID, UserRequestContext.getUserExternalId());
  }

  @Test
  void
      getUserExternalIdFromRequestContext_shouldThrowRequestAttributesException_ifAttributesNotFound() {
    assertThrows(RequestAttributesException.class, UserRequestContext::getUserExternalId);
  }
}
