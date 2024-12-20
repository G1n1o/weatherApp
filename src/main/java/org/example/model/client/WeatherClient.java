package org.example.model.client;

import org.example.model.Weather;

public interface WeatherClient {
    public Weather getWeather(String cityName);
}
