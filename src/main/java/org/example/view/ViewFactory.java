package org.example.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.BaseController;
import org.example.controller.MainWindowController;

import java.io.IOException;
import java.util.Objects;

public class ViewFactory {

    public void showWindow() {
        System.out.println("show window called");
        BaseController controller = new MainWindowController(this, "/view/MainWindow.fxml");
        initializeStage(controller);
    }

    void initializeStage(BaseController baseController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(baseController.getFxmlName()));
        fxmlLoader.setController(baseController);

        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(parent);
        Stage stage = new Stage();

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/view/css/styles.css")).toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();


    }
}
