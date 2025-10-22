package com.zerobase.memobackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Memo Backend API")
                        .version("1.0.0")
                        .description("텍스트 메모 및 검색 기능을 제공하는 RESTful API 서비스")
                        .contact(new Contact()
                                .name("ZeroBase")
                                .email("contact@zerobase.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("로컬 개발 서버"),
                        new Server()
                                .url("https://your-app.herokuapp.com")
                                .description("Heroku 프로덕션 서버")
                ));
    }

}

