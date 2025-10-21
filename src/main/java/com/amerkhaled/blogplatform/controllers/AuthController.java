package com.amerkhaled.blogplatform.controllers;

import com.amerkhaled.blogplatform.domain.dtos.AuthResponseDto;
import com.amerkhaled.blogplatform.domain.dtos.LoginRequestDto;
import com.amerkhaled.blogplatform.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth/login")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        UserDetails userDetails = authenticationService.authenticate(
                loginRequestDto.email(),
                loginRequestDto.password()
        );
        String tokenValue = authenticationService.generateToken(userDetails);

        AuthResponseDto authResponseDto = new AuthResponseDto(
                tokenValue,
                86400L
        );

        return ResponseEntity.ok(authResponseDto);
    }
}