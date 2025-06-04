package com.jo2k.garagify.config.jackson;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public JsonNullableModule jacksonNullableModule() {
        return new org.openapitools.jackson.nullable.JsonNullableModule();
    }
}
