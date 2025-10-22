package com.fileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.fileservice.config.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class FileUploadServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileUploadServiceApplication.class, args);
    }
}

