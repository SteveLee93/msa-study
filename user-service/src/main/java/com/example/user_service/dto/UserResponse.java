package com.example.user_service.dto;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String email,
        String name,
        boolean valid) {
}
