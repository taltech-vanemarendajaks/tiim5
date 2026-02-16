package com.studyplanner.mapper;

import com.studyplanner.dto.UserResponse;
import com.studyplanner.entity.User;
import java.util.List;

public class UserMapper {

  public static UserResponse mapToUserResponse(User user) {
    return UserResponse.builder().externalId(user.getExternalId()).name(user.getName()).build();
  }

  public static List<UserResponse> mapToUserResponseList(List<User> users) {
    return users.stream().map(UserMapper::mapToUserResponse).toList();
  }
}
