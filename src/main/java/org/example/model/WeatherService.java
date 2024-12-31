package org.example.model;

import org.example.model.client.WeatherClient;

import java.util.List;

public class WeatherService {

    private final WeatherClient weatherClient;

    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public List<Weather> getWeeklyWeather(String cityName) {
        return weatherClient.getWeeklyWeather(cityName);
    }
}
