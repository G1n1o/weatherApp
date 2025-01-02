package org.example.model.client;

import org.example.model.Weather;
import org.example.model.exceptions.LocationNotFoundException;
import org.example.model.exceptions.WeatherFetchException;

import java.util.List;

public interface WeatherClient {
    List<Weather> getWeeklyWeather(String cityName) throws LocationNotFoundException, WeatherFetchException;
}
