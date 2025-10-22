package com.weather.service;

import com.weather.dto.ForecastResponse;
import com.weather.dto.WeatherResponse;
import com.weather.dto.external.OpenWeatherCurrentResponse;
import com.weather.dto.external.OpenWeatherForecastResponse;
import com.weather.entity.Forecast;
import com.weather.entity.Location;
import com.weather.entity.WeatherData;
import com.weather.exception.ResourceNotFoundException;
import com.weather.repository.ForecastRepository;
import com.weather.repository.LocationRepository;
import com.weather.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WeatherService {

    private final OpenWeatherMapService openWeatherMapService;
    private final LocationRepository locationRepository;
    private final WeatherDataRepository weatherDataRepository;
    private final ForecastRepository forecastRepository;

    public WeatherResponse getCurrentWeather(String city, String country) {
        log.info("Getting current weather for city: {}, country: {}", city, country);

        // Fetch from external API
        OpenWeatherCurrentResponse apiResponse = openWeatherMapService.getCurrentWeather(city, country);

        // Save or update location
        Location location = saveOrUpdateLocation(apiResponse);

        // Save weather data
        WeatherData weatherData = saveWeatherData(apiResponse, location);

        // Convert to response DTO
        return convertToWeatherResponse(weatherData);
    }

    public ForecastResponse getForecast(String city, String country, Integer days) {
        log.info("Getting forecast for city: {}, country: {}, days: {}", city, country, days);

        // Fetch from external API
        OpenWeatherForecastResponse apiResponse = openWeatherMapService.getForecast(city, country);

        // Save or update location
        Location location = saveOrUpdateLocationFromForecast(apiResponse);

        // Clear old forecasts for this location
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureLimit = now.plusDays(days != null ? days : 5);
        forecastRepository.deleteByLocationAndForecastTimestampBetween(location, now, futureLimit);

        // Save forecast data
        List<Forecast> forecasts = saveForecastData(apiResponse, location, days);

        // Convert to response DTO
        return convertToForecastResponse(location, forecasts);
    }

    @Transactional(readOnly = true)
    public WeatherResponse getWeatherHistory(Long locationId) {
        log.info("Getting weather history for location ID: {}", locationId);

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + locationId));

        WeatherData latestWeather = weatherDataRepository.findTopByLocationOrderByTimestampDesc(location)
                .orElseThrow(() -> new ResourceNotFoundException("No weather data found for location"));

        return convertToWeatherResponse(latestWeather);
    }

    private Location saveOrUpdateLocation(OpenWeatherCurrentResponse response) {
        String city = response.getName();
        String country = response.getSys() != null ? response.getSys().getCountry() : null;

        return locationRepository.findByCityIgnoreCaseAndCountryIgnoreCase(city, country != null ? country : "")
                .map(existing -> {
                    // Update existing location
                    if (response.getCoord() != null) {
                        existing.setLatitude(response.getCoord().getLat());
                        existing.setLongitude(response.getCoord().getLon());
                    }
                    existing.setTimezoneOffset(response.getTimezone());
                    return locationRepository.save(existing);
                })
                .orElseGet(() -> {
                    // Create new location
                    Location newLocation = Location.builder()
                            .city(city)
                            .country(country != null ? country : "")
                            .countryCode(country)
                            .latitude(response.getCoord() != null ? response.getCoord().getLat() : null)
                            .longitude(response.getCoord() != null ? response.getCoord().getLon() : null)
                            .timezoneOffset(response.getTimezone())
                            .build();
                    return locationRepository.save(newLocation);
                });
    }

    private Location saveOrUpdateLocationFromForecast(OpenWeatherForecastResponse response) {
        String city = response.getCity().getName();
        String country = response.getCity().getCountry();

        return locationRepository.findByCityIgnoreCaseAndCountryIgnoreCase(city, country != null ? country : "")
                .map(existing -> {
                    // Update existing location
                    if (response.getCity().getCoord() != null) {
                        existing.setLatitude(response.getCity().getCoord().getLat());
                        existing.setLongitude(response.getCity().getCoord().getLon());
                    }
                    existing.setTimezoneOffset(response.getCity().getTimezone());
                    return locationRepository.save(existing);
                })
                .orElseGet(() -> {
                    // Create new location
                    Location newLocation = Location.builder()
                            .city(city)
                            .country(country != null ? country : "")
                            .countryCode(country)
                            .latitude(response.getCity().getCoord() != null ? response.getCity().getCoord().getLat() : null)
                            .longitude(response.getCity().getCoord() != null ? response.getCity().getCoord().getLon() : null)
                            .timezoneOffset(response.getCity().getTimezone())
                            .build();
                    return locationRepository.save(newLocation);
                });
    }

    private WeatherData saveWeatherData(OpenWeatherCurrentResponse response, Location location) {
        LocalDateTime timestamp = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(response.getDt()), ZoneId.systemDefault());

        WeatherData weatherData = WeatherData.builder()
                .location(location)
                .timestamp(timestamp)
                .temperature(response.getMain().getTemp())
                .feelsLike(response.getMain().getFeelsLike())
                .tempMin(response.getMain().getTempMin())
                .tempMax(response.getMain().getTempMax())
                .pressure(response.getMain().getPressure())
                .humidity(response.getMain().getHumidity())
                .windSpeed(response.getWind() != null ? response.getWind().getSpeed() : null)
                .windDirection(response.getWind() != null ? response.getWind().getDeg() : null)
                .cloudiness(response.getClouds() != null ? response.getClouds().getAll() : null)
                .weatherMain(response.getWeather() != null && !response.getWeather().isEmpty() 
                        ? response.getWeather().get(0).getMain() : null)
                .weatherDescription(response.getWeather() != null && !response.getWeather().isEmpty() 
                        ? response.getWeather().get(0).getDescription() : null)
                .weatherIcon(response.getWeather() != null && !response.getWeather().isEmpty() 
                        ? response.getWeather().get(0).getIcon() : null)
                .visibility(response.getVisibility())
                .rain1h(response.getRain() != null ? response.getRain().getOneHour() : null)
                .snow1h(response.getSnow() != null ? response.getSnow().getOneHour() : null)
                .build();

        return weatherDataRepository.save(weatherData);
    }

    private List<Forecast> saveForecastData(OpenWeatherForecastResponse response, Location location, Integer days) {
        int maxDays = days != null ? days : 5;
        LocalDateTime cutoff = LocalDateTime.now().plusDays(maxDays);

        return response.getList().stream()
                .limit(maxDays * 8) // 8 forecasts per day (3-hour intervals)
                .map(item -> {
                    LocalDateTime forecastTime = LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(item.getDt()), ZoneId.systemDefault());

                    if (forecastTime.isAfter(cutoff)) {
                        return null;
                    }

                    Forecast forecast = Forecast.builder()
                            .location(location)
                            .forecastTimestamp(forecastTime)
                            .temperature(item.getMain().getTemp())
                            .feelsLike(item.getMain().getFeelsLike())
                            .tempMin(item.getMain().getTempMin())
                            .tempMax(item.getMain().getTempMax())
                            .pressure(item.getMain().getPressure())
                            .humidity(item.getMain().getHumidity())
                            .windSpeed(item.getWind() != null ? item.getWind().getSpeed() : null)
                            .windDirection(item.getWind() != null ? item.getWind().getDeg() : null)
                            .cloudiness(item.getClouds() != null ? item.getClouds().getAll() : null)
                            .weatherMain(item.getWeather() != null && !item.getWeather().isEmpty() 
                                    ? item.getWeather().get(0).getMain() : null)
                            .weatherDescription(item.getWeather() != null && !item.getWeather().isEmpty() 
                                    ? item.getWeather().get(0).getDescription() : null)
                            .weatherIcon(item.getWeather() != null && !item.getWeather().isEmpty() 
                                    ? item.getWeather().get(0).getIcon() : null)
                            .visibility(item.getVisibility())
                            .pop(item.getPop())
                            .rain3h(item.getRain() != null ? item.getRain().getThreeHour() : null)
                            .snow3h(item.getSnow() != null ? item.getSnow().getThreeHour() : null)
                            .build();

                    return forecastRepository.save(forecast);
                })
                .filter(f -> f != null)
                .collect(Collectors.toList());
    }

    private WeatherResponse convertToWeatherResponse(WeatherData weatherData) {
        Location location = weatherData.getLocation();
        
        WeatherResponse.LocationDto locationDto = WeatherResponse.LocationDto.builder()
                .id(location.getId())
                .city(location.getCity())
                .country(location.getCountry())
                .countryCode(location.getCountryCode())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();

        return WeatherResponse.builder()
                .id(weatherData.getId())
                .location(locationDto)
                .timestamp(weatherData.getTimestamp())
                .temperature(weatherData.getTemperature())
                .feelsLike(weatherData.getFeelsLike())
                .tempMin(weatherData.getTempMin())
                .tempMax(weatherData.getTempMax())
                .pressure(weatherData.getPressure())
                .humidity(weatherData.getHumidity())
                .windSpeed(weatherData.getWindSpeed())
                .windDirection(weatherData.getWindDirection())
                .cloudiness(weatherData.getCloudiness())
                .weatherMain(weatherData.getWeatherMain())
                .weatherDescription(weatherData.getWeatherDescription())
                .weatherIcon(weatherData.getWeatherIcon())
                .visibility(weatherData.getVisibility())
                .rain1h(weatherData.getRain1h())
                .snow1h(weatherData.getSnow1h())
                .build();
    }

    private ForecastResponse convertToForecastResponse(Location location, List<Forecast> forecasts) {
        ForecastResponse.LocationDto locationDto = ForecastResponse.LocationDto.builder()
                .id(location.getId())
                .city(location.getCity())
                .country(location.getCountry())
                .countryCode(location.getCountryCode())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();

        List<ForecastResponse.ForecastItemDto> forecastItems = forecasts.stream()
                .map(f -> ForecastResponse.ForecastItemDto.builder()
                        .id(f.getId())
                        .forecastTimestamp(f.getForecastTimestamp())
                        .temperature(f.getTemperature())
                        .feelsLike(f.getFeelsLike())
                        .tempMin(f.getTempMin())
                        .tempMax(f.getTempMax())
                        .pressure(f.getPressure())
                        .humidity(f.getHumidity())
                        .windSpeed(f.getWindSpeed())
                        .windDirection(f.getWindDirection())
                        .cloudiness(f.getCloudiness())
                        .weatherMain(f.getWeatherMain())
                        .weatherDescription(f.getWeatherDescription())
                        .weatherIcon(f.getWeatherIcon())
                        .visibility(f.getVisibility())
                        .pop(f.getPop())
                        .rain3h(f.getRain3h())
                        .snow3h(f.getSnow3h())
                        .build())
                .collect(Collectors.toList());

        return ForecastResponse.builder()
                .location(locationDto)
                .forecasts(forecastItems)
                .build();
    }
}

