package it.unical.informatica.progettouid;

import it.unical.informatica.progettouid.model.AdminInitializer;
import it.unical.informatica.progettouid.util.ThemeManager;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import javafx.application.Application;
import javafx.stage.Stage;
import it.unical.informatica.progettouid.controller.client.ImpostazioniController;

public class FlexFit extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SceneHandlerPrimaPagina.getInstance().init(stage);
    }
    /*public void start(Stage primaryStage) {
        try {
            SceneHandlerPrimaPagina sceneHandler = SceneHandlerPrimaPagina.getInstance();
            sceneHandler.init(primaryStage);
        } catch (Exception e) {
            e.printStackTr
            System.exit(1);
        }
    }*/

    public static void main(String[] args) {
        AdminInitializer adminInitializer = new AdminInitializer();
        adminInitializer.initializeAdmins();
        // String theme = ThemeManager.getCurrentTheme();
        // updateTheme(theme);
        launch(args);
    }
}

