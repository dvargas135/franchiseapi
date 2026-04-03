package com.dan.franchiseapi.auth.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtService jwtService;

    public JwtAuthenticationManager(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        if (!jwtService.isTokenValid(token)) {
            return Mono.empty();
        }

        String username = jwtService.extractUsername(token);

        Authentication authenticated = new UsernamePasswordAuthenticationToken(
                username,
                token,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        return Mono.just(authenticated);
    }

}
