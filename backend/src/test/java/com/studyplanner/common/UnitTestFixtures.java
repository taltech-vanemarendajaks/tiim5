package com.studyplanner.common;

import com.studyplanner.dto.UserResponse;
import com.studyplanner.entity.User;
import java.time.LocalDateTime;
import java.util.UUID;

public class UnitTestFixtures {
  public static final UUID A_USER_EXTERNAL_ID =
      UUID.fromString("669b65ca-34bf-4a29-9e5e-1daeedcd4d62");
  public static final LocalDateTime A_LOCAL_DATE_TIME = LocalDateTime.of(2024, 8, 13, 15, 30);

  public static UserResponse aUserResponse() {
    return UserResponse.builder()
        .externalId(A_USER_EXTERNAL_ID)
        .name("User")
        .creationDate(A_LOCAL_DATE_TIME)
        .build();
  }

  public static User aUser() {
    return User.builder()
        .externalId(A_USER_EXTERNAL_ID)
        .name("User")
        .creationDate(A_LOCAL_DATE_TIME)
        .build();
  }
}
