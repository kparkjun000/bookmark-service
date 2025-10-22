package com.weather.service;

import com.weather.dto.external.OpenWeatherCurrentResponse;
import com.weather.dto.external.OpenWeatherForecastResponse;
import com.weather.exception.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class OpenWeatherMapService {

    private final WebClient webClient;
    private final String apiKey;
    private final int timeout;

    public OpenWeatherMapService(
            WebClient weatherWebClient,
            @Value("${weather.api.openweathermap.key}") String apiKey,
            @Value("${weather.api.openweathermap.timeout:5000}") int timeout) {
        this.webClient = weatherWebClient;
        this.apiKey = apiKey;
        this.timeout = timeout;
    }

    @Cacheable(value = "currentWeather", key = "#city + '-' + #country")
    public OpenWeatherCurrentResponse getCurrentWeather(String city, String country) {
        log.info("Fetching current weather for city: {}, country: {}", city, country);

        String query = country != null && !country.isEmpty() 
                ? city + "," + country 
                : city;

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/weather")
                            .queryParam("q", query)
                            .queryParam("appid", apiKey)
                            .queryParam("units", "metric")
                            .build())
                    .retrieve()
                    .bodyToMono(OpenWeatherCurrentResponse.class)
                    .timeout(Duration.ofMillis(timeout))
                    .blockOptional()
                    .orElseThrow(() -> new ExternalApiException("No response from weather API"));

        } catch (WebClientResponseException e) {
            log.error("Weather API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ExternalApiException("Failed to fetch weather data: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error calling weather API", e);
            throw new ExternalApiException("Unexpected error fetching weather data: " + e.getMessage());
        }
    }

    @Cacheable(value = "forecast", key = "#city + '-' + #country")
    public OpenWeatherForecastResponse getForecast(String city, String country) {
        log.info("Fetching forecast for city: {}, country: {}", city, country);

        String query = country != null && !country.isEmpty() 
                ? city + "," + country 
                : city;

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/forecast")
                            .queryParam("q", query)
                            .queryParam("appid", apiKey)
                            .queryParam("units", "metric")
                            .build())
                    .retrieve()
                    .bodyToMono(OpenWeatherForecastResponse.class)
                    .timeout(Duration.ofMillis(timeout))
                    .blockOptional()
                    .orElseThrow(() -> new ExternalApiException("No response from forecast API"));

        } catch (WebClientResponseException e) {
            log.error("Forecast API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ExternalApiException("Failed to fetch forecast data: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error calling forecast API", e);
            throw new ExternalApiException("Unexpected error fetching forecast data: " + e.getMessage());
        }
    }
}

