package com.studyplanner.mapper;

import com.studyplanner.dto.UserResponse;
import com.studyplanner.entity.User;
import java.util.List;

public class UserMapper {

  public static UserResponse mapToResponse(User user) {
    return UserResponse.builder()
        .externalId(user.getExternalId())
        .name(user.getName())
        .creationDate(user.getCreationDate())
        .build();
  }

  public static List<UserResponse> mapToResponseList(List<User> users) {
    return users.stream().map(UserMapper::mapToResponse).toList();
  }
}
