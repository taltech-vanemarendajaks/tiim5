package com.studyplanner.controller;

import com.studyplanner.dto.*;
import com.studyplanner.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "User")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponse> getCurrentUser() {
    return ResponseEntity.ok(userService.getCurrentUser());
  }

  @PostMapping("/register")
  public ResponseEntity<UserResponse> createNewUser(
      @RequestBody @Valid RegisterUserRequest registerUserRequest) {
    return ResponseEntity.ok(userService.createNewUser(registerUserRequest));
  }
}
