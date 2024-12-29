package org.example.model.client;

import org.example.model.Weather;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;


public class ExampleWeatherClient implements WeatherClient  {

    @Override
    public Weather getWeather(String cityName) {

        JSONObject weatherData = getWeatherData(cityName);

        if (weatherData == null) {
            return null;
        }

        double temperature = (double) weatherData.get("temperature");
        String weatherCode = (String) weatherData.get("weather_condition");


        return new Weather(cityName, temperature, LocalDate.now(), weatherCode);
    }
    public static JSONObject getWeatherData (String cityName){
        JSONArray locationData = getLocationData(cityName);

        if (locationData == null || locationData.isEmpty()) {
            System.out.println("Error: No location data found for city: " + cityName);
            return null;
        }

        JSONObject location =(JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&daily=weather_code,temperature_2m_max";
        try{
            HttpURLConnection conn = fetchApiResponse(urlString);
            if(conn.getResponseCode() !=200) {
                System.out.println("Error: Could not connect to API");
                return null;
            }
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }
            scanner.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            JSONObject daily = (JSONObject) resultJsonObj.get("daily");
            JSONArray time = (JSONArray) daily.get("time");
            int index = findIndexOfCurrentTime(time);


            //get temperature
            JSONArray temperatureData = (JSONArray) daily.get("temperature_2m_max");
            double  temperature = (double) temperatureData.get(index);
            //get weather code
            JSONArray weatherCode = (JSONArray) daily.get("weather_code");
            String weatherCondition = convertWeatherCode((long) weatherCode.get(index));

            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);

            return weatherData;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }




    private static JSONArray getLocationData(String cityName) {
        cityName = cityName.replaceAll(" ", "+");
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
        cityName + "&count=10&language=en&format=json";
        try {
            HttpURLConnection conn = fetchApiResponse(urlString);
            if(conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            } else {
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());

                while (scanner.hasNext()){
                    resultJson.append(scanner.nextLine());
                }

                scanner.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
                return locationData;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = String.valueOf(getCurrentTime());
        for(int i = 0; i < timeList.size(); i++ ) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)){
                return i;
            }
        }


        return 0;
}

    private static LocalDate getCurrentTime() {
        return LocalDate.now();
    }

    private static String convertWeatherCode(long weatherCode) {
        String weatherCondition = "Unknown";

        if(weatherCode == 0L){
            weatherCondition = "Clear";
        }else if(weatherCode > 0L && weatherCode <= 3L){
            weatherCondition = "Cloudy";
        }else if(weatherCode >= 45L && weatherCode <= 48L){
            weatherCondition = "Fog";
        }else if((weatherCode >= 51L && weatherCode <= 67L)
                || (weatherCode >= 80L && weatherCode <= 99L)){
            weatherCondition = "Rain";
        }else if(weatherCode >= 71L && weatherCode <= 77L){
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }
}


