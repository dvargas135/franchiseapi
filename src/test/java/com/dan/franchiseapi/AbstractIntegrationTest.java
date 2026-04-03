package com.dan.franchiseapi;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIntegrationTest {

    @SuppressWarnings("resource")
    static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.4")
            .withDatabaseName("franchise_db")
            .withUsername("franchise_user")
            .withPassword("franchise_pass")
            .withInitScript("sql/001-schema.sql");

    static {
        MYSQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.r2dbc.url",
                () -> String.format(
                        "r2dbc:mysql://%s:%d/%s",
                        MYSQL_CONTAINER.getHost(),
                        MYSQL_CONTAINER.getMappedPort(3306),
                        MYSQL_CONTAINER.getDatabaseName()
                )
        );
        registry.add("spring.r2dbc.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.r2dbc.password", MYSQL_CONTAINER::getPassword);
    }
}
