package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.view.ViewFactory;


public class App extends Application
{
    public static void main( String[] args )
    {
        launch(args);
    }

    public void start (Stage stage) throws Exception{

        ViewFactory viewFactory = new ViewFactory();
        viewFactory.showWindow();

    }
}
