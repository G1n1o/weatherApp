package org.example.controller;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
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
import org.example.model.DataStorage;
import org.example.model.Weather;
import org.example.model.WeatherService;
import org.example.model.WeatherServiceFactory;
import org.example.model.exceptions.LocationNotFoundException;
import org.example.view.ViewFactory;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainWindowController extends BaseController implements Initializable {

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
    private Label startLabel;

    @FXML
    private Label destinationCityName;

    @FXML
    private ImageView todayImage;

    @FXML
    private HBox weeklyForecastContainer;

    @FXML
    private HBox destinationForecast;

    @FXML
    private Button destiantionSearchButton;

    @FXML
    private TextField destinationCityNameField;

    private WeatherService weatherService;

    public MainWindowController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        weatherService = WeatherServiceFactory.createWeatherService();

        String formattedDate = formatDateWithSuffix(LocalDate.now());
        date.setText(formattedDate);
        startClock();

        try {
            FileInputStream fis = new FileInputStream("save.dat");
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);

            DataStorage dStorage = (DataStorage) ois.readObject();
            ois.close();
            cityName.setText(dStorage.getCityName());

        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (cityName.getText() != null && !cityName.getText().isEmpty()) {
            cityValidationAndTakeWeather(cityName.getText(), weeklyForecastContainer);
        }

        cityNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                saveButton.setDisable(false);
            }
        });
    }

    @FXML
    void takeWeather() {
        String inputCity = cityNameField.getText().trim();
        if (inputCity.isEmpty()) {
            errorLabel.setText("City name is empty. Please enter a valid city name.");
            return;
        }

        errorLabel.setText("");

        // Attempt to fetch weather data and handle errors
        if(cityValidationAndTakeWeather(cityNameField.getText(), weeklyForecastContainer)){
            cityName.setText(inputCity);
            cityNameField.clear();
        }


    }

    @FXML
    void destiantionSearch() {

        // Handle destination search with error handling
                if(cityValidationAndTakeWeather(destinationCityNameField.getText(),destinationForecast)) {
                    destinationCityName.setText(destinationCityNameField.getText());
                    destinationCityNameField.clear();
                }
    }

    private boolean cityValidationAndTakeWeather(String cityName, HBox container) {
        try {
            List<Weather> weeklyWeather = weatherService.getWeeklyWeather(cityName);
            if (weeklyWeather == null || weeklyWeather.isEmpty()) {
                errorLabel.setText("No data available for this location. Try another location.");
                return false;
            }
            createWeatherForecastView(weeklyWeather, container);
        } catch (LocationNotFoundException e) {
            errorLabel.setText("Location not found. Please enter a valid city.");
            System.err.println("Location not found: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            errorLabel.setText("Error fetching weather data. Please try again.");
            System.err.println("Runtime error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            errorLabel.setText("Unexpected error occurred. Please try again.");
            System.err.println("Unexpected error: " + e.getMessage());
            return false;
        }
        return true;
    }

    private void createWeatherForecastView(List<Weather> weeklyWeather, HBox container) {

        container.getChildren().clear();
        container.setVisible(true);

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

                ImageView weatherImage = new ImageView(new Image(Objects.requireNonNull(forecast.getClass().getResource("/view/img/" + forecast.getWeatherCondition() + ".png")).toExternalForm()));
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

                ImageView weatherImage = new ImageView(new Image(Objects.requireNonNull(forecast.getClass().getResource("/view/img/" + forecast.getWeatherCondition() + ".png")).toExternalForm()));
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
            container.getChildren().add(dayBox);
        }
    }

    @FXML
    void saveCity() {
        try {
            FileOutputStream fos = new FileOutputStream("save.dat");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            DataStorage dStorage = new DataStorage(cityName.getText());

            oos.writeObject(dStorage);
            oos.close();

            errorLabel.setText("Successfully saved the name of your city");
            errorLabel.setStyle("-fx-text-fill: green;");

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> {
                errorLabel.setStyle("-fx-text-fill: red;"); // Reset style
                errorLabel.setText("");  // Clear message
            });
            pause.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
