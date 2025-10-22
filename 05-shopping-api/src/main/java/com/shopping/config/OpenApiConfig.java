package com.shopping.config;

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
        Server localServer = new Server();
        localServer.setUrl("http://localhost:" + serverPort);
        localServer.setDescription("Local Development Server");

        Server productionServer = new Server();
        productionServer.setUrl("https://your-app-name.herokuapp.com");
        productionServer.setDescription("Production Server");

        Contact contact = new Contact();
        contact.setName("Shopping Mall API Team");
        contact.setEmail("support@shopping-api.com");

        License license = new License();
        license.setName("MIT License");
        license.setUrl("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Shopping Mall API")
                .version("1.0.0")
                .description("RESTful API for Shopping Mall Product and Order Management System. " +
                        "This API provides comprehensive endpoints for managing products, categories, " +
                        "shopping carts, orders, and payments.")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, productionServer));
    }
}

