package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application
{
    public static void main( String[] args )
    {
        launch(args);
    }

    public void start (Stage stage) throws Exception{
        Parent parent = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));

        Scene scene = new Scene(parent, 600,400);
        stage.setScene(scene);
        stage.show();

    }
}
