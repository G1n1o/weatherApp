package org.example.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.model.Weather;
import org.example.model.WeatherService;
import org.example.model.WeatherServiceFactory;
import org.example.view.ViewFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class WindowController extends BaseController implements Initializable {

    @FXML
    private VBox body;

    @FXML
    private Label cityName;

    @FXML
    private Label clock;

    @FXML
    private Label date;

    private WeatherService weatherService;

    public WindowController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        weatherService = WeatherServiceFactory.createWeatherService();
        Weather weather = weatherService.getWeather(cityName.getText());

        String formattedDate = formatDateWithSuffix(weather.getDate());
        date.setText(formattedDate);

        startClock();
    }



    private String formatDateWithSuffix(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy", Locale.ENGLISH);
        int day = date.getDayOfMonth();
        return date.format(formatter).replaceFirst("\\d+", day + getDaySuffix(day));
    }

    private String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }

    private void startClock() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Tworzenie mechanizmu aktualizacji co sekundę
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            String currentTime = LocalTime.now().format(timeFormatter);
            clock.setText(currentTime);
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play(); // Uruchom timeline
    }

}
