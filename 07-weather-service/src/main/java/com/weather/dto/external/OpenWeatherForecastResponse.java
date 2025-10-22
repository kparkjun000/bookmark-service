package com.weather.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherForecastResponse {

    @JsonProperty("list")
    private List<ForecastItem> list;

    @JsonProperty("city")
    private City city;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForecastItem {
        @JsonProperty("dt")
        private Long dt;

        @JsonProperty("main")
        private Main main;

        @JsonProperty("weather")
        private List<Weather> weather;

        @JsonProperty("clouds")
        private Clouds clouds;

        @JsonProperty("wind")
        private Wind wind;

        @JsonProperty("visibility")
        private Double visibility;

        @JsonProperty("pop")
        private Double pop;

        @JsonProperty("rain")
        private Rain rain;

        @JsonProperty("snow")
        private Snow snow;

        @JsonProperty("dt_txt")
        private String dtTxt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        @JsonProperty("temp")
        private Double temp;

        @JsonProperty("feels_like")
        private Double feelsLike;

        @JsonProperty("temp_min")
        private Double tempMin;

        @JsonProperty("temp_max")
        private Double tempMax;

        @JsonProperty("pressure")
        private Integer pressure;

        @JsonProperty("humidity")
        private Integer humidity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        @JsonProperty("id")
        private Integer id;

        @JsonProperty("main")
        private String main;

        @JsonProperty("description")
        private String description;

        @JsonProperty("icon")
        private String icon;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Clouds {
        @JsonProperty("all")
        private Integer all;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        @JsonProperty("speed")
        private Double speed;

        @JsonProperty("deg")
        private Integer deg;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rain {
        @JsonProperty("3h")
        private Double threeHour;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Snow {
        @JsonProperty("3h")
        private Double threeHour;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class City {
        @JsonProperty("name")
        private String name;

        @JsonProperty("coord")
        private Coord coord;

        @JsonProperty("country")
        private String country;

        @JsonProperty("timezone")
        private Integer timezone;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coord {
        @JsonProperty("lat")
        private Double lat;

        @JsonProperty("lon")
        private Double lon;
    }
}

