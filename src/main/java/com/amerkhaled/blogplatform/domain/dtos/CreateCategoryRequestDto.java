package com.amerkhaled.blogplatform.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
        @NotBlank(message = "Category name is required")
        @Size(min = 2, max = 50, message = "Category must be between {min} and {max} characters")
        @Pattern(regexp = "^[\\w\\s-]+$", message = "Category name can only contain letters, numbers, spaces, and hyphens")
        String name
) {
}
