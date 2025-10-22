package com.fileservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI fileServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("File Upload and Image Processing API")
                        .description("RESTful API for file upload and image processing with thumbnail generation")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("File Service Team")
                                .email("support@fileservice.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development"),
                        new Server().url("https://sheltered-refuge-08256-1a1e88fad567.herokuapp.com").description("Production")
                ));
    }
}

