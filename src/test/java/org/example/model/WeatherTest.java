package org.example.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WeatherTest {

    @Test
    void shouldReturnCorrectDayOfWeek() {
        // given
        Weather weather1 = new Weather("City A", 15.5, LocalDate.of(2025, 1, 1), "Sunny");  // 1st Jan 2025 -> Wednesday
        Weather weather2 = new Weather("City A", 10.0, LocalDate.of(2025, 1, 2), "Cloudy");  // 2nd Jan 2025 -> Thursday
        Weather weather3 = new Weather("City A", 8.0, LocalDate.of(2025, 1, 3), "Rainy");    // 3rd Jan 2025 -> Friday

        // when
        String dayOfWeek1 = weather1.getDayOfWeek();
        String dayOfWeek2 = weather2.getDayOfWeek();
        String dayOfWeek3 = weather3.getDayOfWeek();

        // then
        assertEquals("WED", dayOfWeek1);  // Should return "WED" for Wednesday
        assertEquals("THU", dayOfWeek2);  // Should return "THU" for Thursday
        assertEquals("FRI", dayOfWeek3);  // Should return "FRI" for Friday
    }

    @Test
    void shouldReturnCorrectWeatherCondition() {
        // given
        Weather weather1 = new Weather("City A", 15.5, LocalDate.of(2025, 1, 1), "Sunny");
        Weather weather2 = new Weather("City A", 10.0, LocalDate.of(2025, 1, 2), "Cloudy");
        Weather weather3 = new Weather("City A", 8.0, LocalDate.of(2025, 1, 3), "Rainy");

        // when
        String weatherCondition1 = weather1.getWeatherCondition();
        String weatherCondition2 = weather2.getWeatherCondition();
        String weatherCondition3 = weather3.getWeatherCondition();

        // then
        assertEquals("Sunny", weatherCondition1);  // Should return "Sunny"
        assertEquals("Cloudy", weatherCondition2); // Should return "Cloudy"
        assertEquals("Rainy", weatherCondition3);  // Should return "Rainy"
    }
}