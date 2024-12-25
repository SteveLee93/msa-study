package com.example.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.LoginRequest;
import com.example.user_service.dto.SignupRequest;
import com.example.user_service.dto.UserResponse;
import com.example.user_service.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
    UserResponse response = userService.signup(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
  @PostMapping("/validate")
    public UserResponse validateUser(@RequestBody LoginRequest request) {
      // 내부 통신용 엔드포인트
      return userService.validateUser(request);
  }
}
