package com.example.auth_service.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.auth_service.config.JwtTokenProvider;
import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.TokenResponse;
import com.example.auth_service.dto.UserResponse;
import com.example.auth_service.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
  private final RestTemplate restTemplate;
  private final JwtTokenProvider jwtTokenProvider;

  public TokenResponse login(LoginRequest request) {
    try {
      log.info("Sending validation request to User Service for email: {}", request.email());

      ResponseEntity<UserResponse> response = restTemplate.exchange(
          "http://user-service:8082/api/users/validate",
          HttpMethod.POST,
          new HttpEntity<>(request),
          UserResponse.class);

      log.info("Received response from User Service: {}", response.getStatusCode());
      UserResponse userResponse = response.getBody();
      log.debug("User response: {}", userResponse);

      if (userResponse == null || !userResponse.valid()) {
        throw new UnauthorizedException("Invalid credentials");
      }

      String token = jwtTokenProvider.createToken(userResponse.email());
      return new TokenResponse(token);

    } catch (HttpClientErrorException e) {
      log.error("User service validation failed", e);
      throw new UnauthorizedException("Invalid credentials");
    } catch (Exception e) {
      log.error("Login failed", e);
      throw new UnauthorizedException("Login failed: " + e.getMessage());
    }
  }
}