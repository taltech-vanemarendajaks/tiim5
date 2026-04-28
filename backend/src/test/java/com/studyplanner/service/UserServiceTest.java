package com.studyplanner.service;

import static com.studyplanner.common.UnitTestFixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.studyplanner.repository.UserRepository;
import com.studyplanner.utils.UserRequestContext;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private CurriculumService curriculumService;
  @Mock private StudyPlanService studyPlanService;
  @InjectMocks private UserService userService;

  @Test
  void getAllUsersTest() {
    var user = List.of(aUser());
    var userResponse = List.of(aUserResponse());

    when(userRepository.findAll()).thenReturn(user);

    var actual = userService.getAllUsers();

    assertEquals(userResponse, actual);
  }

  @Test
  void getCurrentUserTest() {
    var user = aUser();
    var userResponse = aUserResponse();
    try (var mockedRequestContext = mockStatic(UserRequestContext.class)) {
      mockedRequestContext
          .when(UserRequestContext::getUserExternalId)
          .thenReturn(A_USER_EXTERNAL_ID);
      when(userRepository.findByExternalId(A_USER_EXTERNAL_ID)).thenReturn(user);

      var actual = userService.getCurrentUser();

      assertEquals(userResponse, actual);
    }
  }

  @Test
  void createNewUserTest() {
    var request = aRegisterUserRequest();
    var savedUser = aUser();
    when(userRepository.save(any())).thenReturn(savedUser);

    var actual = userService.createNewUser(request);

    assertEquals(aUserResponse(), actual);
    verify(userRepository).save(any());
  }
}
