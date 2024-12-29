package org.example.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Label errorLabel;

    @FXML
    private Label clock;

    @FXML
    private Label date;
    @FXML
    private TextField cityNameField;

    @FXML
    private Button saveButton;

    @FXML
    private Label homeTodayTemp;

    @FXML
    private ImageView todayImage;



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



        cityNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.isEmpty()) {
                    saveButton.setDisable(false);
                }
            }
        });


    }

    @FXML
    void takeWeather() {
        System.out.println("Click");
        String inputCity = cityNameField.getText().trim();
        if (inputCity.isEmpty()) {
            errorLabel.setText("City name is empty. Please enter a valid city name.");
            return;
        }

        errorLabel.setText("");

        cityName.setText(inputCity);

        try {
            Weather weather = weatherService.getWeather(cityName.getText());
            if (weather == null) {
                System.out.println("Error: Weather data could not be retrieved.");
                errorLabel.setText("No data available for this location. Try another location");
                return;
            }

            homeTodayTemp.setText(String.format("%.1f°", weather.getTempInCelsius()));

            try {
                Image newImage = new Image(getClass().getResource("/view/img/" + weather.getWeatherCode() + ".png").toExternalForm());
                todayImage.setImage(newImage);

            } catch (Exception e) {
                System.err.println("/view/img/" + weather.getWeatherCode() + ".png");
                todayImage.setImage(new Image(getClass().getResource("/view/img/Unknown.png").toExternalForm()));
                errorLabel.setText("Weather image not found.");
                e.printStackTrace();
            }

        } catch (Exception e) {
            errorLabel.setText("Error fetching weather data. Please try again.");
            System.err.println("Error fetching weather data: " + e.getMessage());
            e.printStackTrace();
        }


        cityNameField.clear();
    }

    @FXML
    void saveCity() {
        System.out.println("Click");
        saveButton.setDisable(true);
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

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            String currentTime = LocalTime.now().format(timeFormatter);
            clock.setText(currentTime);
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


}
