package com.amerkhaled.blogplatform.domain.dtos;

import java.util.List;

public record ApiErrorResponseDto(
        Integer status,
        String message,
        List<FieldError> errors
) {

    public record FieldError(String field, String message) {

    }

}
