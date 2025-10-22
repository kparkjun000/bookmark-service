package com.weather.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Location request for weather query")
public class LocationRequest {

    @NotBlank(message = "City name is required")
    @Schema(description = "City name", example = "Seoul")
    private String city;

    @Schema(description = "Country code (optional, 2-letter ISO 3166)", example = "KR")
    private String country;
}

