package com.studyplanner.controller;

import com.studyplanner.dto.UserResponse;
import com.studyplanner.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<UserResponse>> getAll() {
    return ResponseEntity.ok(userService.getAllUsers());
  }
}
