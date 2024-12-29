package it.unical.informatica.progettouid.view;

import it.unical.informatica.progettouid.FlexFit;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

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
        this.stage = primaryStage;
        this.stage.setTitle("FlexFit Gym");

        this.mainPane = new BorderPane();
        this.scene = new Scene(mainPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.stage.setScene(scene);
        this.stage.setWidth(WINDOW_WIDTH);
        this.stage.setHeight(WINDOW_HEIGHT);


        loadPrimaPagina();
        this.stage.show();
    }

    public void loadPrimaPagina() throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/primaPagina.fxml"));
            Parent root = loader.load();
            mainPane.getChildren().clear();
            mainPane.setCenter(root);
            stage.setTitle("FlexFit Gym");
        } catch (Exception e) {
            System.err.println(STR."Errore nel caricamento della prima pagina: \{e.getMessage()}");
            throw e;
        }
    }

    public void loadLoginScreen(UserType userType) throws Exception {
        String fxmlPath = switch (userType) {
            case CLIENT -> "/fxml/client/clientLogin.fxml";
            case ADMIN -> "/fxml/admin/adminLogin.fxml";
            case TRAINER -> "/fxml/pt/trainerLogin.fxml";
        };

        try {
            FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource(fxmlPath));
            Parent view = loader.load();
            mainPane.getChildren().clear();
            mainPane.setCenter(view);
            stage.setTitle("Login " + getUserTypeTitle(userType));
        } catch (Exception e) {
            System.err.println("Errore nel caricamento della pagina di login: " + e.getMessage());
            System.err.println("Path tentato: " + fxmlPath);
            throw e;
        }
    }

    public void loadMainInterface(UserType userType) throws Exception {
        String fxmlPath = switch (userType) {
            case CLIENT -> "/fxml/client/dashboardClient.fxml";
            case ADMIN -> "/fxml/admin/dashboardAdmin.fxml";
            case TRAINER -> "/fxml/pt/dashboardTrainer.fxml";
        };

        try {
            FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource(fxmlPath));
            Parent view = loader.load();
            mainPane.setCenter(view);
            stage.setTitle("Dashboard " + getUserTypeTitle(userType));
        } catch (Exception e) {
            System.err.println("Errore nel caricamento della dashboard: " + e.getMessage());
            System.err.println("Path tentato: " + fxmlPath);
            throw e;
        }
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