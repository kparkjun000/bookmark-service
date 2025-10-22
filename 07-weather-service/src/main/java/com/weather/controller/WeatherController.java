package com.weather.controller;

import com.weather.dto.ApiResponse;
import com.weather.dto.ForecastResponse;
import com.weather.dto.LocationRequest;
import com.weather.dto.WeatherResponse;
import com.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Weather", description = "Weather API endpoints")
public class WeatherController {

    private final WeatherService weatherService;

    @Operation(
        summary = "Get current weather",
        description = "Retrieve current weather information for a specific location"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved weather data",
            content = @Content(schema = @Schema(implementation = WeatherResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "502",
            description = "External API error"
        )
    })
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<WeatherResponse>> getCurrentWeather(
            @Parameter(description = "City name", required = true, example = "Seoul")
            @RequestParam String city,
            @Parameter(description = "Country code (optional, 2-letter ISO 3166)", example = "KR")
            @RequestParam(required = false) String country) {
        
        log.info("GET /api/v1/weather/current - city: {}, country: {}", city, country);
        WeatherResponse weather = weatherService.getCurrentWeather(city, country);
        return ResponseEntity.ok(ApiResponse.success("Weather data retrieved successfully", weather));
    }

    @Operation(
        summary = "Get current weather (POST)",
        description = "Retrieve current weather information for a specific location using POST method"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved weather data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid request body"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "502",
            description = "External API error"
        )
    })
    @PostMapping("/current")
    public ResponseEntity<ApiResponse<WeatherResponse>> getCurrentWeatherPost(
            @Valid @RequestBody LocationRequest request) {
        
        log.info("POST /api/v1/weather/current - request: {}", request);
        WeatherResponse weather = weatherService.getCurrentWeather(
                request.getCity(), request.getCountry());
        return ResponseEntity.ok(ApiResponse.success("Weather data retrieved successfully", weather));
    }

    @Operation(
        summary = "Get weather forecast",
        description = "Retrieve weather forecast for a specific location (up to 5 days)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved forecast data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "502",
            description = "External API error"
        )
    })
    @GetMapping("/forecast")
    public ResponseEntity<ApiResponse<ForecastResponse>> getForecast(
            @Parameter(description = "City name", required = true, example = "Seoul")
            @RequestParam String city,
            @Parameter(description = "Country code (optional, 2-letter ISO 3166)", example = "KR")
            @RequestParam(required = false) String country,
            @Parameter(description = "Number of days for forecast (1-5)", example = "5")
            @RequestParam(required = false, defaultValue = "5") Integer days) {
        
        log.info("GET /api/v1/weather/forecast - city: {}, country: {}, days: {}", 
                city, country, days);
        
        if (days < 1 || days > 5) {
            days = 5;
        }
        
        ForecastResponse forecast = weatherService.getForecast(city, country, days);
        return ResponseEntity.ok(ApiResponse.success("Forecast data retrieved successfully", forecast));
    }

    @Operation(
        summary = "Get weather forecast (POST)",
        description = "Retrieve weather forecast for a specific location using POST method"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved forecast data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid request body"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "502",
            description = "External API error"
        )
    })
    @PostMapping("/forecast")
    public ResponseEntity<ApiResponse<ForecastResponse>> getForecastPost(
            @Valid @RequestBody LocationRequest request,
            @Parameter(description = "Number of days for forecast (1-5)", example = "5")
            @RequestParam(required = false, defaultValue = "5") Integer days) {
        
        log.info("POST /api/v1/weather/forecast - request: {}, days: {}", request, days);
        
        if (days < 1 || days > 5) {
            days = 5;
        }
        
        ForecastResponse forecast = weatherService.getForecast(
                request.getCity(), request.getCountry(), days);
        return ResponseEntity.ok(ApiResponse.success("Forecast data retrieved successfully", forecast));
    }

    @Operation(
        summary = "Get weather history",
        description = "Retrieve the latest stored weather data for a specific location by ID"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved weather history"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Location not found"
        )
    })
    @GetMapping("/history/{locationId}")
    public ResponseEntity<ApiResponse<WeatherResponse>> getWeatherHistory(
            @Parameter(description = "Location ID", required = true)
            @PathVariable Long locationId) {
        
        log.info("GET /api/v1/weather/history/{}", locationId);
        WeatherResponse weather = weatherService.getWeatherHistory(locationId);
        return ResponseEntity.ok(ApiResponse.success("Weather history retrieved successfully", weather));
    }
}

