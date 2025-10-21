package com.amerkhaled.blogplatform.domain.dtos;

import java.util.UUID;

public record TagDto(
        UUID id,
        String name,
        Long postCount
) {
}
