package com.zerobase.bookmarkservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI bookmarkServiceOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("로컬 개발 서버");
        
        Server productionServer = new Server();
        productionServer.setUrl("https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com");
        productionServer.setDescription("프로덕션 서버 (Heroku)");
        
        Contact contact = new Contact();
        contact.setEmail("admin@bookmarkservice.com");
        contact.setName("Bookmark Service Team");
        contact.setUrl("https://github.com/zerobase/bookmark-service");
        
        License license = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");
        
        Info info = new Info()
                .title("Bookmark Service API")
                .version("1.0.0")
                .contact(contact)
                .description("URL 북마크 관리 및 태그 서비스 API 문서입니다. " +
                            "이 API를 통해 북마크를 저장하고, 메타데이터를 추출하며, " +
                            "태그를 관리하고, 공개 북마크를 공유할 수 있습니다.")
                .termsOfService("https://bookmarkservice.com/terms")
                .license(license);
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, productionServer));
    }
}

