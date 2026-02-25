package com.studyplanner.service;

import com.studyplanner.dto.UserResponse;
import com.studyplanner.mapper.UserMapper;
import com.studyplanner.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public List<UserResponse> getAllUsers() {
    return UserMapper.mapToResponseList(userRepository.findAll());
  }
}
