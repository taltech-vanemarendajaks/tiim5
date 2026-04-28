package com.studyplanner.service;

import com.studyplanner.dto.*;
import com.studyplanner.entity.*;
import com.studyplanner.mapper.UserMapper;
import com.studyplanner.repository.UserRepository;
import com.studyplanner.utils.*;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public List<UserResponse> getAllUsers() {
    return UserMapper.mapToResponseList(userRepository.findAll());
  }

  public UserResponse createNewUser(RegisterUserRequest registerUserRequest) {
    User user = new User();
    String name = registerUserRequest.name();

    user.setName(name);
    user.setExternalId(UUID.randomUUID());

    User savedUser = userRepository.save(user);
    return UserMapper.mapToResponse(savedUser);
  }

  public User getUserByExternalId(UUID externalId) {
    return userRepository.findByExternalId(externalId);
  }

  public UserResponse getCurrentUser() {
    UUID userExternalId = UserRequestContext.getUserExternalId();
    return UserMapper.mapToResponse(this.getUserByExternalId(userExternalId));
  }
}
