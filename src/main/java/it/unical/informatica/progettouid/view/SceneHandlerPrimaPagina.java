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

    public Stage getStage() {
        return stage;
    }

    // Enumerazione per i tipi di utente
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

        // Carica la prima pagina come root
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/primaPagina.fxml"));
        Parent root = loader.load();
        this.mainPane = new BorderPane();
        this.mainPane.setCenter(root);

        this.scene = new Scene(mainPane, 1000, 600);
        this.stage.setScene(scene);
        this.stage.setWidth(1000);
        this.stage.setHeight(600);
        this.stage.show();
    }

    public void loadLoginScreen(UserType userType) throws Exception {
        String fxmlPath = switch (userType) {
            case CLIENT -> "/fxml/client/ClientLogin.fxml";
            case ADMIN -> "/fxml/admin/adminLogin.fxml";
            case TRAINER -> "/fxml/pt/PTLogin.fxml";
        };

        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource(fxmlPath));
        Parent view = loader.load();
        mainPane.setCenter(view);
        stage.setTitle("Login " + getUserTypeTitle(userType));
    }

    public void loadMainInterface(UserType userType) throws Exception {
        String fxmlPath = switch (userType) {
            case CLIENT -> "/fxml/client/dashboardClient.fxml";
            case ADMIN -> "/fxml/admin/aggiungiUtente.fxml";
            case TRAINER -> "/fxml/pt/dashboardPT.fxml";
        };

        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource(fxmlPath));
        Parent view = loader.load();
        mainPane.setCenter(view);
        stage.setTitle("Dashboard " + getUserTypeTitle(userType));
    }

    private String getUserTypeTitle(UserType userType) {
        return switch (userType) {
            case CLIENT -> "Cliente";
            case ADMIN -> "Amministratore";
            case TRAINER -> "Personal Trainer";
        };
    }
}