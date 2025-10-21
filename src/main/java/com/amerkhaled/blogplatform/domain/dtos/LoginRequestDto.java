package com.amerkhaled.blogplatform.domain.dtos;

public record LoginRequestDto(
        String email,
        String password
) {
}
