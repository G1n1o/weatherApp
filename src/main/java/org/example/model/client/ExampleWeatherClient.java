package org.example.model.client;

import org.example.model.Weather;

import java.time.LocalDate;

public class ExampleWeatherClient implements WeatherClient  {
    @Override
    public Weather getWeather(String cityName) {
        return new Weather(cityName, 5, LocalDate.now());
    }
}
