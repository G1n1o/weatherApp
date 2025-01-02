package org.example.model;

import org.example.model.client.WeatherClient;
import org.example.model.exceptions.LocationNotFoundException;
import org.example.model.exceptions.WeatherFetchException;

import java.util.List;

public class WeatherService {

    private final WeatherClient weatherClient;

    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public List<Weather> getWeeklyWeather(String cityName) throws LocationNotFoundException, WeatherFetchException {
        return weatherClient.getWeeklyWeather(cityName);
    }
}
