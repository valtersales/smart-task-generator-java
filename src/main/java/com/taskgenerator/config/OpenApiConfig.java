package com.taskgenerator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI Configuration
 */
@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Smart Task Generator API")
                                                .version("1.0.0")
                                                .description("API for intelligent task list generation using AI")
                                                .contact(new Contact()
                                                                .name("Valter Sales")
                                                                .url("https://github.com/valtersales"))
                                                .license(new License()
                                                                .name("MIT License")
                                                                .url("https://opensource.org/licenses/MIT")));
        }
}
