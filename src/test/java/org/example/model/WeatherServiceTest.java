package org.example.model;

import org.example.model.client.WeatherClient;
import org.example.model.exceptions.LocationNotFoundException;
import org.example.model.exceptions.WeatherFetchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherServiceTest {

    private WeatherService weatherService;
    private WeatherClient mockWeatherClient;

    @BeforeEach
    void setUp() {
        mockWeatherClient = mock(WeatherClient.class);
        weatherService = new WeatherService(mockWeatherClient);
    }

    @Test
    void shouldReturnWeatherDataForCityWhenValidCityNameIsProvided() throws LocationNotFoundException, WeatherFetchException {
        // given
        List<Weather> mockWeatherList = Arrays.asList(
                new Weather("City A", 15.5, LocalDate.of(2025, 1, 1), "Sunny"),
                new Weather("City A", 10.0, LocalDate.of(2025, 1, 2), "Cloudy"),
                new Weather("City A", 8.0, LocalDate.of(2025, 1, 3), "Rainy")
        );

        // Mockowanie klienta
        when(mockWeatherClient.getWeeklyWeather("City A")).thenReturn(mockWeatherList);

        // when
        List<Weather> result = weatherService.getWeeklyWeather("City A");

        // then
        assertEquals(3, result.size()); // Sprawdzenie liczby elementów
        assertEquals("Sunny", result.get(0).getWeatherCondition()); // Sprawdzenie warunku pogodowego
        assertEquals("WED", result.get(0).getDayOfWeek()); // Sprawdzenie dnia tygodnia dla 2025-01-01
        verify(mockWeatherClient, times(1)).getWeeklyWeather("City A"); // Sprawdzenie wywołania mocka
    }

    @Test
    void shouldThrowWeatherFetchExceptionWhenWeatherFetchFails() throws WeatherFetchException, LocationNotFoundException {
        // given
        when(mockWeatherClient.getWeeklyWeather("Unknown City"))
                .thenThrow(new LocationNotFoundException("Location not found"));

        // when & then
        Exception exception = assertThrows(LocationNotFoundException.class, () -> {
            weatherService.getWeeklyWeather("Unknown City");
        });

        // then
        assertEquals("Location not found", exception.getMessage());
        verify(mockWeatherClient, times(1)).getWeeklyWeather("Unknown City");
    }

    @Test
    void testGetWeeklyWeather_WeatherFetchError() throws LocationNotFoundException, WeatherFetchException {
        // when
        when(mockWeatherClient.getWeeklyWeather("City B"))
                .thenThrow(new WeatherFetchException("Weather fetch error"));

        // then
        Exception exception = assertThrows(WeatherFetchException.class, () -> {
            weatherService.getWeeklyWeather("City B");
        });

        assertEquals("Weather fetch error", exception.getMessage());
        verify(mockWeatherClient, times(1)).getWeeklyWeather("City B");
    }

}