package com.jo2k.Garaze.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPIFromYaml() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("openapi.yaml");

        if (inputStream == null) {
            throw new RuntimeException("❌ openapi.yaml not found in classpath!");
        }

        String yaml = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return new OpenAPIV3Parser().readContents(yaml, null, null).getOpenAPI();
    }
}