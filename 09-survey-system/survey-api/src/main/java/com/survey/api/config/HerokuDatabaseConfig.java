package com.survey.api.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class HerokuDatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        
        System.out.println("DATABASE_URL: " + (databaseUrl != null ? "Found" : "Not found"));
        
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            try {
                if (databaseUrl.startsWith("postgres://")) {
                    URI dbUri = new URI(databaseUrl);
                    
                    String username = dbUri.getUserInfo().split(":")[0];
                    String password = dbUri.getUserInfo().split(":")[1];
                    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath()
                            + "?sslmode=require";
                    
                    System.out.println("Parsed JDBC URL: " + dbUrl.replaceAll("\\?.*", ""));
                    
                    return DataSourceBuilder
                            .create()
                            .url(dbUrl)
                            .username(username)
                            .password(password)
                            .driverClassName("org.postgresql.Driver")
                            .build();
                } else if (databaseUrl.startsWith("jdbc:")) {
                    // Already in JDBC format
                    System.out.println("Using JDBC URL directly");
                    return DataSourceBuilder
                            .create()
                            .url(databaseUrl)
                            .driverClassName("org.postgresql.Driver")
                            .build();
                }
            } catch (Exception e) {
                System.err.println("Failed to parse DATABASE_URL: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Database configuration error", e);
            }
        }
        
        // Local development fallback
        System.out.println("Using local database configuration");
        return DataSourceBuilder
                .create()
                .url("jdbc:postgresql://localhost:5432/surveydb")
                .username("postgres")
                .password("postgres")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}

