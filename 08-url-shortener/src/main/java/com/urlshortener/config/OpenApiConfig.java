package com.urlshortener.config;

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

    @Value("${app.base-url}")
    private String baseUrl;

    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl(baseUrl);
        server.setDescription("URL Shortener Service");

        Contact contact = new Contact();
        contact.setName("Zero-Base 13주차");
        contact.setEmail("aparkjun@naver.com");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("URL Shortener & Analytics API")
                .version("1.0.0")
                .description("URL 단축, 리디렉션, 클릭 통계 및 분석 서비스 API 문서입니다. " +
                        "URL을 단축하고, QR 코드를 생성하며, 클릭 이벤트를 추적하고 통계를 분석할 수 있습니다.")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}

