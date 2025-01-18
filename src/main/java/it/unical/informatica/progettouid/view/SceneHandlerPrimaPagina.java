package it.unical.informatica.progettouid.view;

import it.unical.informatica.progettouid.FlexFit;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneHandlerPrimaPagina {
    private static SceneHandlerPrimaPagina instance = null;
    private Scene scene;
    private Stage stage;
    private BorderPane mainPane;
    private static final double WINDOW_WIDTH = 1000;
    private static final double WINDOW_HEIGHT = 600;

    public Stage getStage() {
        return stage;
    }

    public enum UserType {
        CLIENT,
        ADMIN,
        TRAINER
    }

    private SceneHandlerPrimaPagina() {}

    public static SceneHandlerPrimaPagina getInstance() {
        if (instance == null) {
            instance = new SceneHandlerPrimaPagina();
        }
        return instance;
    }

    public void init(Stage primaryStage) throws Exception {
        if (this.stage == null) {
            this.stage = primaryStage;
            this.scene = new Scene(loadFXML("/fxml/primaPagina.fxml"), WINDOW_WIDTH, WINDOW_HEIGHT);
            this.stage.setTitle("Flexfit");
            this.stage.setScene(scene);
            this.stage.show();
        }
    }

    public <T> T loadFXML(String fxmlFile) {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource(fxmlFile));
        try {
            return loader.load();
        } catch (Exception e) {
            System.err.println("Errore nel caricamento del file FXML: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void loadPrimaPagina() throws Exception {
        if(this.scene != null){
            this.scene.setRoot(loadFXML("/fxml/primaPagina.fxml"));
        }
    }

    public void loadLoginScreen(UserType userType) throws Exception {
        String fxmlPath = switch (userType) {
            case CLIENT -> "/fxml/client/clientLogin.fxml";
            case ADMIN -> "/fxml/admin/adminLogin.fxml";
            case TRAINER -> "/fxml/pt/trainerLogin.fxml";
        };
        if (this.scene != null) {
            this.scene.setRoot(loadFXML(fxmlPath));
        }
/*
        try {
            System.out.println("Tentativo di caricamento del file FXML: " + fxmlPath);
            URL resource = FlexFit.class.getResource(fxmlPath);

            if (resource == null) {
                throw new IllegalStateException("File FXML non trovato: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(resource);

            Parent view = loader.load();
            mainPane.getChildren().clear();
            mainPane.setCenter(view);
            stage.setTitle("Login " + getUserTypeTitle(userType));
        } catch (Exception e) {
            System.err.println("Errore nel caricamento della pagina di login: " + e.getMessage());
            System.err.println("Path tentato: " + fxmlPath);
            throw e;
        }*/
    }



    public void switchToAdminView() throws Exception {
        SceneHandlerAdmin.getInstance().init(stage);
    }

    public void switchToClientView() throws Exception {
        SceneHandlerClient.getInstance().init(stage);
    }

    public void switchToTrainerView() throws Exception {
        // Assumi che esista un SceneHandlerTrainer
        SceneHandlerPT.getInstance().init(stage);
    }

    private String getUserTypeTitle(UserType userType) {
        return switch (userType) {
            case CLIENT -> "Cliente";
            case ADMIN -> "Amministratore";
            case TRAINER -> "Personal Trainer";
        };
    }
}