package com.amerkhaled.blogplatform.domain.dtos;

import java.util.UUID;

public record CategoryDto(
    UUID id,
    String name,
    Long postCount
) {
}
