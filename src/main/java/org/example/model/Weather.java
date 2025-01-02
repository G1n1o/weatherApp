package org.example.model;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

public class Weather {

    private final String cityName;
    private final double tempInCelsius;
    private final LocalDate date;
    private final String weatherCondition;



    public Weather(String cityName, double tempInCelsius, LocalDate date, String weatherCondition) {
        this.cityName = cityName;
        this.tempInCelsius = tempInCelsius;
        this.date = date;
        this.weatherCondition = weatherCondition;
    }

    public double getTempInCelsius() {
        return tempInCelsius;
    }

    public String getDayOfWeek() {
        return date.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                .toUpperCase();
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather = (Weather) o;
        return Double.compare(tempInCelsius, weather.tempInCelsius) == 0 && Objects.equals(cityName, weather.cityName) && Objects.equals(date, weather.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, tempInCelsius, date);
    }

    @Override
    public String toString() {
        return "Weather{" +
                "cityName='" + cityName + '\'' +
                ", tempInCelsius=" + tempInCelsius +
                ", date=" + date +
                '}';
    }

}
