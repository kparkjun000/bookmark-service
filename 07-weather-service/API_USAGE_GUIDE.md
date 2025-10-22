# Weather Service API 사용 가이드

## 빠른 시작

### 1. Swagger UI 접근

애플리케이션을 실행한 후 브라우저에서 다음 URL로 접속하세요:

**로컬**: http://localhost:8080/swagger-ui.html

**Heroku**: https://your-app-name.herokuapp.com/swagger-ui.html

## API 엔드포인트 상세 가이드

### 1. Health Check

#### GET /api/v1/health

서비스 상태를 확인합니다.

**요청 예시:**

```bash
curl http://localhost:8080/api/v1/health
```

**응답 예시:**

```json
{
  "success": true,
  "message": "Service is healthy",
  "data": {
    "status": "UP",
    "timestamp": "2024-05-01T12:00:00",
    "service": "Weather Service API",
    "version": "1.0.0"
  },
  "timestamp": "2024-05-01T12:00:00"
}
```

### 2. 현재 날씨 조회

#### GET /api/v1/weather/current

**파라미터:**

- `city` (필수): 도시명
- `country` (선택): 국가 코드 (ISO 3166, 2글자)

**요청 예시:**

```bash
# 도시명만 사용
curl "http://localhost:8080/api/v1/weather/current?city=Seoul"

# 도시명과 국가 코드 사용
curl "http://localhost:8080/api/v1/weather/current?city=Seoul&country=KR"

# 다른 도시 예시
curl "http://localhost:8080/api/v1/weather/current?city=Tokyo&country=JP"
curl "http://localhost:8080/api/v1/weather/current?city=London&country=GB"
curl "http://localhost:8080/api/v1/weather/current?city=New%20York&country=US"
```

**응답 예시:**

```json
{
  "success": true,
  "message": "Weather data retrieved successfully",
  "data": {
    "id": 1,
    "location": {
      "id": 1,
      "city": "Seoul",
      "country": "KR",
      "countryCode": "KR",
      "latitude": 37.5665,
      "longitude": 126.978
    },
    "timestamp": "2024-05-01T12:00:00",
    "temperature": 18.5,
    "feelsLike": 17.8,
    "tempMin": 15.0,
    "tempMax": 20.0,
    "pressure": 1013,
    "humidity": 65,
    "windSpeed": 3.5,
    "windDirection": 180,
    "cloudiness": 40,
    "weatherMain": "Clouds",
    "weatherDescription": "scattered clouds",
    "weatherIcon": "03d",
    "visibility": 10000.0,
    "rain1h": null,
    "snow1h": null
  },
  "timestamp": "2024-05-01T12:00:00"
}
```

#### POST /api/v1/weather/current

**요청 Body:**

```json
{
  "city": "Seoul",
  "country": "KR"
}
```

**요청 예시:**

```bash
curl -X POST "http://localhost:8080/api/v1/weather/current" \
  -H "Content-Type: application/json" \
  -d '{
    "city": "Seoul",
    "country": "KR"
  }'
```

### 3. 날씨 예보 조회

#### GET /api/v1/weather/forecast

**파라미터:**

- `city` (필수): 도시명
- `country` (선택): 국가 코드 (ISO 3166, 2글자)
- `days` (선택): 예보 일수 (1-5일, 기본값: 5)

**요청 예시:**

```bash
# 5일 예보 (기본값)
curl "http://localhost:8080/api/v1/weather/forecast?city=Seoul&country=KR"

# 3일 예보
curl "http://localhost:8080/api/v1/weather/forecast?city=Seoul&country=KR&days=3"

# 1일 예보
curl "http://localhost:8080/api/v1/weather/forecast?city=Tokyo&country=JP&days=1"
```

**응답 예시:**

```json
{
  "success": true,
  "message": "Forecast data retrieved successfully",
  "data": {
    "location": {
      "id": 1,
      "city": "Seoul",
      "country": "KR",
      "countryCode": "KR",
      "latitude": 37.5665,
      "longitude": 126.978
    },
    "forecasts": [
      {
        "id": 1,
        "forecastTimestamp": "2024-05-01T15:00:00",
        "temperature": 19.2,
        "feelsLike": 18.5,
        "tempMin": 18.0,
        "tempMax": 20.0,
        "pressure": 1012,
        "humidity": 60,
        "windSpeed": 4.2,
        "windDirection": 200,
        "cloudiness": 35,
        "weatherMain": "Clouds",
        "weatherDescription": "few clouds",
        "weatherIcon": "02d",
        "visibility": 10000.0,
        "pop": 0.1,
        "rain3h": null,
        "snow3h": null
      },
      {
        "id": 2,
        "forecastTimestamp": "2024-05-01T18:00:00",
        "temperature": 17.5,
        "feelsLike": 16.8,
        "tempMin": 16.5,
        "tempMax": 18.5,
        "pressure": 1013,
        "humidity": 65,
        "windSpeed": 3.8,
        "windDirection": 190,
        "cloudiness": 45,
        "weatherMain": "Clouds",
        "weatherDescription": "scattered clouds",
        "weatherIcon": "03d",
        "visibility": 10000.0,
        "pop": 0.05,
        "rain3h": null,
        "snow3h": null
      }
    ]
  },
  "timestamp": "2024-05-01T12:00:00"
}
```

#### POST /api/v1/weather/forecast

**요청 Body:**

```json
{
  "city": "Seoul",
  "country": "KR"
}
```

**요청 예시:**

```bash
curl -X POST "http://localhost:8080/api/v1/weather/forecast?days=3" \
  -H "Content-Type: application/json" \
  -d '{
    "city": "Seoul",
    "country": "KR"
  }'
```

### 4. 날씨 히스토리 조회

#### GET /api/v1/weather/history/{locationId}

저장된 날씨 데이터를 조회합니다.

**파라미터:**

- `locationId` (필수): Location ID (Path Variable)

**요청 예시:**

```bash
curl "http://localhost:8080/api/v1/weather/history/1"
```

## 주요 도시 목록

### 한국

- Seoul (서울): `city=Seoul&country=KR`
- Busan (부산): `city=Busan&country=KR`
- Incheon (인천): `city=Incheon&country=KR`
- Daegu (대구): `city=Daegu&country=KR`
- Jeju (제주): `city=Jeju&country=KR`

### 일본

- Tokyo (도쿄): `city=Tokyo&country=JP`
- Osaka (오사카): `city=Osaka&country=JP`
- Kyoto (교토): `city=Kyoto&country=JP`

### 미국

- New York: `city=New%20York&country=US`
- Los Angeles: `city=Los%20Angeles&country=US`
- Chicago: `city=Chicago&country=US`

### 유럽

- London: `city=London&country=GB`
- Paris: `city=Paris&country=FR`
- Berlin: `city=Berlin&country=DE`

## 날씨 아이콘

응답의 `weatherIcon` 필드는 OpenWeatherMap 아이콘 코드입니다.

아이콘 URL 형식:

```
https://openweathermap.org/img/wn/{icon}@2x.png
```

예시:

```
https://openweathermap.org/img/wn/03d@2x.png
```

## 에러 응답

### 400 Bad Request

잘못된 요청 파라미터

```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "city": "City name is required"
  },
  "timestamp": "2024-05-01T12:00:00"
}
```

### 404 Not Found

리소스를 찾을 수 없음

```json
{
  "success": false,
  "message": "Location not found with ID: 999",
  "data": null,
  "timestamp": "2024-05-01T12:00:00"
}
```

### 502 Bad Gateway

외부 API 에러

```json
{
  "success": false,
  "message": "External API error: Failed to fetch weather data",
  "data": null,
  "timestamp": "2024-05-01T12:00:00"
}
```

## Postman Collection

### 환경 변수 설정

```json
{
  "baseUrl": "http://localhost:8080",
  "city": "Seoul",
  "country": "KR"
}
```

### 요청 예시

1. **현재 날씨 조회 (GET)**

   - Method: GET
   - URL: `{{baseUrl}}/api/v1/weather/current?city={{city}}&country={{country}}`

2. **현재 날씨 조회 (POST)**

   - Method: POST
   - URL: `{{baseUrl}}/api/v1/weather/current`
   - Body (JSON):
     ```json
     {
       "city": "{{city}}",
       "country": "{{country}}"
     }
     ```

3. **날씨 예보 조회**
   - Method: GET
   - URL: `{{baseUrl}}/api/v1/weather/forecast?city={{city}}&country={{country}}&days=5`

## JavaScript 예시

### Fetch API

```javascript
// 현재 날씨 조회
async function getCurrentWeather(city, country) {
  const url = `http://localhost:8080/api/v1/weather/current?city=${city}&country=${country}`;

  try {
    const response = await fetch(url);
    const data = await response.json();

    if (data.success) {
      console.log("Temperature:", data.data.temperature);
      console.log("Weather:", data.data.weatherDescription);
    }
  } catch (error) {
    console.error("Error:", error);
  }
}

// 날씨 예보 조회
async function getForecast(city, country, days = 5) {
  const url = `http://localhost:8080/api/v1/weather/forecast?city=${city}&country=${country}&days=${days}`;

  try {
    const response = await fetch(url);
    const data = await response.json();

    if (data.success) {
      data.data.forecasts.forEach((forecast) => {
        console.log(
          `${forecast.forecastTimestamp}: ${forecast.temperature}°C - ${forecast.weatherDescription}`
        );
      });
    }
  } catch (error) {
    console.error("Error:", error);
  }
}

// 사용 예시
getCurrentWeather("Seoul", "KR");
getForecast("Tokyo", "JP", 3);
```

### Axios

```javascript
const axios = require("axios");

const api = axios.create({
  baseURL: "http://localhost:8080/api/v1",
  timeout: 10000,
});

// 현재 날씨 조회
async function getCurrentWeather(city, country) {
  try {
    const response = await api.get("/weather/current", {
      params: { city, country },
    });
    return response.data;
  } catch (error) {
    console.error("Error:", error.response?.data || error.message);
    throw error;
  }
}

// POST 방식
async function getCurrentWeatherPost(city, country) {
  try {
    const response = await api.post("/weather/current", {
      city,
      country,
    });
    return response.data;
  } catch (error) {
    console.error("Error:", error.response?.data || error.message);
    throw error;
  }
}
```

## Python 예시

```python
import requests

BASE_URL = "http://localhost:8080/api/v1"

def get_current_weather(city, country=None):
    """현재 날씨 조회"""
    params = {"city": city}
    if country:
        params["country"] = country

    response = requests.get(f"{BASE_URL}/weather/current", params=params)

    if response.status_code == 200:
        data = response.json()
        if data["success"]:
            weather = data["data"]
            print(f"Temperature: {weather['temperature']}°C")
            print(f"Weather: {weather['weatherDescription']}")
            return weather
    else:
        print(f"Error: {response.status_code}")
        return None

def get_forecast(city, country=None, days=5):
    """날씨 예보 조회"""
    params = {"city": city, "days": days}
    if country:
        params["country"] = country

    response = requests.get(f"{BASE_URL}/weather/forecast", params=params)

    if response.status_code == 200:
        data = response.json()
        if data["success"]:
            forecasts = data["data"]["forecasts"]
            for forecast in forecasts:
                print(f"{forecast['forecastTimestamp']}: {forecast['temperature']}°C - {forecast['weatherDescription']}")
            return forecasts
    else:
        print(f"Error: {response.status_code}")
        return None

# 사용 예시
if __name__ == "__main__":
    get_current_weather("Seoul", "KR")
    print("\n" + "="*50 + "\n")
    get_forecast("Tokyo", "JP", 3)
```

## 캐싱

API는 자동으로 외부 API 호출을 캐싱합니다:

- 캐시 유효 시간: 1시간 (3600초)
- 같은 도시/국가 조합에 대한 연속 요청은 캐시된 결과 반환

## Rate Limiting

OpenWeatherMap 무료 API 제한:

- 60 calls/minute
- 1,000,000 calls/month

프로젝트의 캐싱 기능으로 API 호출 최소화
