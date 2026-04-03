package com.dan.franchiseapi.franchise;

import com.dan.franchiseapi.AbstractIntegrationTest;
import com.dan.franchiseapi.auth.dto.LoginRequest;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

class FranchiseControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldCreateFranchiseWhenAuthenticated() {
        String responseBody = webTestClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginRequest("admin", "admin123"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        String token = responseBody.replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");

        webTestClient.post()
                .uri("/api/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .bodyValue(Map.of("name", "McDonalds"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNumber()
                .jsonPath("$.name").isEqualTo("McDonalds");
    }

    @Test
    void shouldRejectFranchiseCreationWhenNotAuthenticated() {
        webTestClient.post()
                .uri("/api/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "name": "Burger King"
                        }
                        """)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
