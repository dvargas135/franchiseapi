package com.dan.franchiseapi.auth.config;

import com.dan.franchiseapi.auth.model.AppUser;
import com.dan.franchiseapi.auth.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataInitializer(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        appUserRepository.findByUsername("admin")
                .switchIfEmpty(
                        appUserRepository.save(
                                new AppUser(
                                        "admin",
                                        passwordEncoder.encode("admin123"),
                                        "ADMIN"
                                )
                        )
                )
                .block();
    }
}
