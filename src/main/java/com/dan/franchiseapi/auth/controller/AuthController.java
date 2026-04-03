package com.dan.franchiseapi.auth.controller;

import com.dan.franchiseapi.auth.dto.LoginRequest;
import com.dan.franchiseapi.auth.dto.LoginResponse;
import com.dan.franchiseapi.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
