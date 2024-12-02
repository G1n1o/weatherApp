package org.example.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class MainViewController {

    @FXML
    private AnchorPane destinationCity;

    @FXML
    private TextField homeCity;

    @FXML
    void test() {
        System.out.println("Click!");
    }
}
