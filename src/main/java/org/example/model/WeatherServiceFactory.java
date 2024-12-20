package org.example.model;

import org.example.model.client.ExampleWeatherClient;
import org.example.model.client.WeatherClient;

public class WeatherServiceFactory {

    public static WeatherService createWeatherService() {
        return new WeatherService(createWeatherClient());
    }

    private static WeatherClient createWeatherClient() {
        return new ExampleWeatherClient();
    }
}
