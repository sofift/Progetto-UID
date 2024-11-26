package it.unical.informatica.progettouid;

import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import javafx.application.Application;
import javafx.stage.Stage;


public class FlexFit extends Application {
    @Override
    /*public void start(Stage stage) throws Exception {
        SceneHandlerClient.getInstance().init(stage);
    }*/
    public void start(Stage primaryStage) {
        try {
            SceneHandlerPrimaPagina sceneHandler = SceneHandlerPrimaPagina.getInstance();
            sceneHandler.init(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

