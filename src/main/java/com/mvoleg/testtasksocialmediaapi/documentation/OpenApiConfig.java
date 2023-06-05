package com.mvoleg.testtasksocialmediaapi.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info()
                .title("Social media тестовое задание")
                .description("Решение тестового задания");

        return new OpenAPI().info(info);
    }
}
