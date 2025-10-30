package com.vovgoo.BankSystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bankSystemOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BankSystem API")
                        .version("0.0.1‑SNAPSHOT")
                        .description("Документация API для система управления банковскими средствами"));
    }
}