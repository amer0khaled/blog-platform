package com.amerkhaled.blogplatform.domain.dtos;

import com.amerkhaled.blogplatform.domain.PostStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record PostDto(
        UUID id,
        String title,
        String content,
        AuthorDto author,
        CategoryDto category,
        Set<TagDto> tags,
        Integer readingTime,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        PostStatus status
) {
}
