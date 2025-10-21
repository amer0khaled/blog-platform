package com.amerkhaled.blogplatform.controllers;

import com.amerkhaled.blogplatform.domain.dtos.ApiErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ApiErrorResponseDto> handleException(Exception exception) {
        log.error("Error during processing of request", exception);
        ApiErrorResponseDto errorResponse = new ApiErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "unexpected error occurred",
                null);
        return new ResponseEntity<>(errorResponse,  HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException exception) {
        ApiErrorResponseDto errorResponse = new ApiErrorResponseDto(HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                null);
        return new ResponseEntity<>(errorResponse,  HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponseDto> handleIllegalStateException(IllegalStateException exception) {
        ApiErrorResponseDto errorResponse = new ApiErrorResponseDto(HttpStatus.CONFLICT.value(),
                "Incorrect Username or Password",
                null);
        return new ResponseEntity<>(errorResponse,  HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponseDto> handleBadCredentialsException(BadCredentialsException exception) {
        ApiErrorResponseDto errorResponse = new ApiErrorResponseDto(HttpStatus.UNAUTHORIZED.value(),
                exception.getMessage(),
                null);
        return new ResponseEntity<>(errorResponse,  HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException exception) {
        ApiErrorResponseDto errorResponse = new ApiErrorResponseDto(HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                null);
        return new ResponseEntity<>(errorResponse,  HttpStatus.NOT_FOUND);
    }

}
