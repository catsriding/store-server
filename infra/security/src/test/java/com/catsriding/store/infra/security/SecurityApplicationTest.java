package com.catsriding.store.infra.security;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Tag("context")
@ConfigurationPropertiesScan
@SpringBootApplication
public class SecurityApplicationTest {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplicationTest.class, args);
    }

}
