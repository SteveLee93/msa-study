package com.example.user_service.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.user_service.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_service.dto.LoginRequest;
import com.example.user_service.dto.SignupRequest;
import com.example.user_service.dto.UserResponse;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.exception.EmailAlreadyExistsException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public UserResponse signup(SignupRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new EmailAlreadyExistsException(request.email());
    }

    // 사용자 생성
    User user = User.builder()
        .email(request.email())
        .password(passwordEncoder.encode(request.password()))
        .name(request.name())
        .build();

    userRepository.save(user);

    return new UserResponse(
        user.getId(),
        user.getEmail(),
        user.getName(),
        true);
  }

  public UserResponse validateUser(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    return new UserResponse(
        user.getId(),
        user.getEmail(),
        user.getName(),
        true);
  }
}
