package com.survey.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SurveyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurveyApiApplication.class, args);
    }
}
