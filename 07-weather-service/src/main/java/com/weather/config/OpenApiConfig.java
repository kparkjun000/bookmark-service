package com.weather.config;

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
    public OpenAPI weatherServiceOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:" + serverPort);
        localServer.setDescription("Local Development Server");

        Server prodServer = new Server();
        prodServer.setUrl("https://your-app.herokuapp.com");
        prodServer.setDescription("Production Server (Heroku)");

        Contact contact = new Contact();
        contact.setName("Weather Service Team");
        contact.setEmail("support@weatherservice.com");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Weather Service API")
                .version("1.0.0")
                .description("RESTful API for weather information with external API integration. " +
                        "Provides current weather data and forecast for locations worldwide.")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, prodServer));
    }
}

