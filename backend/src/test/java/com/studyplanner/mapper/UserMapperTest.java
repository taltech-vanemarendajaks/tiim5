package com.studyplanner.mapper;

import static com.studyplanner.common.UnitTestFixtures.aUser;
import static com.studyplanner.common.UnitTestFixtures.aUserResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.List;

class UserMapperTest {
  @Test
  void mapToResponseTest() {
    var user = aUser();
    var userResponse = aUserResponse();

    var actual = UserMapper.mapToResponse(user);

    assertEquals(userResponse, actual);
  }

    @Test
    void mapToResponseListTest() {
        var user = List.of(aUser());
        var userResponse = List.of(aUserResponse());

        var actual = UserMapper.mapToResponseList(user);

        assertEquals(userResponse, actual);
    }
}
