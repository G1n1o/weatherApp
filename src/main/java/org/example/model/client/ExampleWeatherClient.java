package org.example.model.client;

import org.example.model.Weather;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExampleWeatherClient implements WeatherClient {

    @Override
    public List<Weather> getWeeklyWeather(String cityName) {
        JSONArray locationData = getLocationData(cityName);
        if (locationData == null || locationData.isEmpty()) {
            throw new RuntimeException("Error: Unable to retrieve location data for " + cityName);
        }

        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude +
                "&longitude=" + longitude +
                "&daily=weather_code,temperature_2m_max&timezone=auto";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Error: Could not connect to weather API");
            }

            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }
            scanner.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(resultJson.toString());

            JSONObject daily = (JSONObject) resultJsonObj.get("daily");
            JSONArray time = (JSONArray) daily.get("time");
            JSONArray temperatureData = (JSONArray) daily.get("temperature_2m_max");
            JSONArray weatherCodeData = (JSONArray) daily.get("weather_code");

            List<Weather> weeklyWeather = new ArrayList<>();
            for (int i = 0; i < time.size(); i++) {
                LocalDate date = LocalDate.parse((String) time.get(i));
                double temperature = (double) temperatureData.get(i);
                String weatherCondition = convertWeatherCode((long) weatherCodeData.get(i));
                weeklyWeather.add(new Weather(cityName, temperature, date, weatherCondition));
            }

            return weeklyWeather;

        } catch (Exception e) {
            throw new RuntimeException("Error fetching weather data: " + e.getMessage(), e);
        }
    }

    private static JSONArray getLocationData(String cityName) {
        cityName = cityName.replaceAll(" ", "+");
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                cityName + "&count=10&language=en&format=json";
        try {
            HttpURLConnection conn = fetchApiResponse(urlString);
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Error: Could not connect to geocoding API");
            }

            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }
            scanner.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(resultJson.toString());

            return (JSONArray) resultsJsonObj.get("results");

        } catch (Exception e) {
            throw new RuntimeException("Error fetching location data: " + e.getMessage(), e);
        }
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        } catch (IOException e) {
            throw new RuntimeException("Error connecting to API: " + e.getMessage(), e);
        }
    }

    private static String convertWeatherCode(long weatherCode) {
        if (weatherCode == 0L) {
            return "Clear";
        } else if (weatherCode > 0L && weatherCode <= 3L) {
            return "Cloudy";
        } else if (weatherCode >= 45L && weatherCode <= 48L) {
            return "Fog";
        } else if ((weatherCode >= 51L && weatherCode <= 67L)
                || (weatherCode >= 80L && weatherCode <= 99L)) {
            return "Rain";
        } else if (weatherCode >= 71L && weatherCode <= 77L) {
            return "Snow";
        }
        return "Unknown";
    }
}
