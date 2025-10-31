package com.vovgoo.BankSystem.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bankSystemOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BankSystem API")
                        .version("0.0.1-SNAPSHOT")
                        .description("""
                                Документация API для системы управления банковскими средствами.
                                
                                В API доступны следующие модули:
                                - Клиенты: создание, обновление, удаление и поиск клиентов.
                                - Счета: создание, удаление, пополнение, снятие и переводы между счетами.
                                
                                Все операции возвращают подробные ответы с указанием статуса транзакции и текста ошибки в случае отказа.
                                
                                Автор: vovgoo
                                """)
                        .contact(new Contact()
                                .name("vovgoo")
                                .url("https://github.com/vovgoo"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}