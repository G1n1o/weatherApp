package org.example.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.BaseController;
import org.example.controller.MainViewController;
import org.example.controller.WindowController;

import java.io.IOException;

public class ViewFactory {

    public void showMainView() {
        System.out.println("show mainView window called");
        BaseController controller = new MainViewController(this, "/view/MainView.fxml");
        initializeStage(controller);
    }
    public void showWindow() {
        System.out.println("show window called");
        BaseController controller = new WindowController(this, "/view/Window.fxml");
        initializeStage(controller);
    }

    private void initializeStage(BaseController baseController){
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

        scene.getStylesheets().add(getClass().getResource("/view/css/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

    }
}
