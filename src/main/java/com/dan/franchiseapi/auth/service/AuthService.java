package com.dan.franchiseapi.auth.service;

import com.dan.franchiseapi.auth.dto.LoginRequest;
import com.dan.franchiseapi.auth.dto.LoginResponse;
import com.dan.franchiseapi.auth.repository.AppUserRepository;
import com.dan.franchiseapi.auth.security.JwtService;
import com.dan.franchiseapi.common.exception.UnauthorizedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Mono<LoginResponse> login(LoginRequest request) {
        return appUserRepository.findByUsername(request.username())
                .switchIfEmpty(Mono.error(new UnauthorizedException("Invalid username or password")))
                .flatMap(user -> {
                    boolean matches = passwordEncoder.matches(request.password(), user.getPassword());

                    if (!matches) {
                        return Mono.error(new UnauthorizedException("Invalid username or password"));
                    }

                    String token = jwtService.generateToken(user.getUsername(), user.getRole());
                    return Mono.just(new LoginResponse(token));
                });
    }

}
