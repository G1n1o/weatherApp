package org.example.model.client;

import org.example.model.Weather;

import java.util.List;

public interface WeatherClient {
    List<Weather> getWeeklyWeather(String cityName);
}
