package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.aUser;
import static com.studyplanner.common.UnitTestFixtures.aUserResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.studyplanner.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;
  @InjectMocks private UserService userService;

  @Test
  void getAllUsersTest() {
    var user = List.of(aUser());
    var userResponse = List.of(aUserResponse());

    doReturn(user).when(userRepository).findAll();

    var actual = userService.getAllUsers();

    assertEquals(userResponse, actual);
  }
}
