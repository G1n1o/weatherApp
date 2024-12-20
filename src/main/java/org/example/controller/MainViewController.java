package org.example.controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.model.Weather;
import org.example.model.WeatherService;
import org.example.model.WeatherServiceFactory;
import org.example.view.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController extends BaseController implements Initializable {

    @FXML
    private TextField destinationCity;

    @FXML
    private TextField homeCity;

    @FXML
    private Label temperature;

    @FXML
    private Label temperatureLabel;

    private WeatherService weatherService;

    public MainViewController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        weatherService = WeatherServiceFactory.createWeatherService();
        temperature.setVisible(false);
        temperatureLabel.setVisible(false);
    }

    @FXML
    void test() {
        System.out.println("Click!");

        String cityName = "Kalisz";
        Weather weather = weatherService.getWeather(cityName);

        displayWeather(weather);
    }

    private void displayWeather(Weather weather) {
        temperature.setVisible(true);
        temperatureLabel.setVisible(true);
        temperature.setText("" + weather.getTempInCelsius());
    }


}
