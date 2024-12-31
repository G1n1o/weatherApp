package org.example.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
import java.util.List;
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

    @FXML
    private HBox weeklyForecastContainer;



    private WeatherService weatherService;

    public WindowController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        weatherService = WeatherServiceFactory.createWeatherService();

        String formattedDate = formatDateWithSuffix(LocalDate.now());
        date.setText(formattedDate);
        startClock();

        if (cityName.getText() != null && !cityName.getText().isEmpty()) {
            cityValidationAndTakeWeather(cityName.getText());
        }


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

        cityValidationAndTakeWeather(cityName.getText());

        cityNameField.clear();
    }

    private void cityValidationAndTakeWeather(String cityName) {

        try {
            List<Weather> weeklyWeather = weatherService.getWeeklyWeather(cityName);
            if (weeklyWeather == null || weeklyWeather.isEmpty()) {
                System.out.println("Error: Weather data could not be retrieved.");
                errorLabel.setText("No data available for this location. Try another location");
                return;
            }

            createWeatherForecastView(weeklyWeather);

        } catch (Exception e) {
            errorLabel.setText("Error fetching weather data. Please try again.");
            System.err.println("Error fetching weather data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createWeatherForecastView(List<Weather> weeklyWeather) {

        weeklyForecastContainer.getChildren().clear();

        for (int i = 0; i < weeklyWeather.size(); i++) {
            Weather forecast = weeklyWeather.get(i);

            VBox dayBox;

            if (i == 0) {
                // First day
                dayBox = new VBox();
                dayBox.setPrefHeight(200.0);
                dayBox.setPrefWidth(180.0);

                HBox dayRow = new HBox();
                dayRow.setAlignment(Pos.CENTER);
                dayRow.setPrefHeight(131.0);
                dayRow.setPrefWidth(180.0);

                ImageView weatherImage = new ImageView(new Image(forecast.getClass().getResource("/view/img/" + forecast.getWeatherCondition() + ".png").toExternalForm()));
                weatherImage.setFitHeight(65.0);
                weatherImage.setFitWidth(65.0);
                weatherImage.setPickOnBounds(true);
                weatherImage.setPreserveRatio(true);

                VBox dayTextBox = new VBox();
                dayTextBox.setAlignment(Pos.CENTER);
                dayTextBox.setPrefHeight(200.0);
                dayTextBox.setPrefWidth(100.0);
                dayTextBox.setSpacing(10.0);

                Label todayLabel = new Label("TODAY");
                todayLabel.getStyleClass().add("days_background");
                todayLabel.setPrefWidth(70.0);
                dayTextBox.getChildren().add(todayLabel);

                Label tempLabel = new Label(forecast.getTempInCelsius() + "°");
                tempLabel.getStyleClass().add("firstDay_temperature");
                dayTextBox.getChildren().add(tempLabel);

                dayRow.getChildren().addAll(weatherImage, dayTextBox);
                dayBox.getChildren().add(dayRow);

            } else {
                // Another days
                dayBox = new VBox();
                dayBox.setAlignment(Pos.CENTER);
                dayBox.setPrefHeight(200.0);
                dayBox.setPrefWidth(100.0);
                dayBox.setSpacing(10.0);

                Label dayLabel = new Label(forecast.getDayOfWeek());
                dayLabel.setPrefHeight(45.0);
                dayLabel.setPrefWidth(60.0);
                dayLabel.getStyleClass().add("days_background");

                ImageView weatherImage = new ImageView(new Image(forecast.getClass().getResource("/view/img/" + forecast.getWeatherCondition() + ".png").toExternalForm()));
                weatherImage.setFitHeight(101.0);
                weatherImage.setFitWidth(65.0);
                weatherImage.setPickOnBounds(true);
                weatherImage.setPreserveRatio(true);

                Label tempLabel = new Label(forecast.getTempInCelsius() + "°");
                tempLabel.setPrefHeight(91.0);
                tempLabel.setPrefWidth(100.0);
                tempLabel.getStyleClass().add("otherDays_temperature");

                dayBox.getChildren().addAll(dayLabel, weatherImage, tempLabel);
            }
            weeklyForecastContainer.getChildren().add(dayBox);
        }
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
