package com.amerkhaled.blogplatform.domain.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreateTagsRequestDto(
        @Size(max = 10, message = "maximum {max} tags allowed")
        @NotEmpty(message = "at least one tag name required")
        Set<
        @Pattern(regexp = "^[\\w\\s-]+$", message = "Tag name can only contain letters, numbers, spaces, and hyphens")
        @Size(min = 2, max = 30, message = "Tag must be between {min} and {max} characters") String> names
) {
}
