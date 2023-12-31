package com.aktie.kotlincredit.configuration

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun publicApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .group("spring-kotlin-credit")
            .pathsToMatch("/api/v1/**")
            .build()
    }

}