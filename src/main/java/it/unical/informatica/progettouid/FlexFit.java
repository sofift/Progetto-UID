package it.unical.informatica.progettouid;

import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FlexFit extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SceneHandlerClient.getInstance().init(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}