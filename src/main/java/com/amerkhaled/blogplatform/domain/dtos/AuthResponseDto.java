package com.amerkhaled.blogplatform.domain.dtos;

public record AuthResponseDto(
        String token,
        Long expiresIn
) {
}
