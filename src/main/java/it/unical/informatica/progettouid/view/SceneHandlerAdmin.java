package it.unical.informatica.progettouid.view;

import it.unical.informatica.progettouid.FlexFit;
import it.unical.informatica.progettouid.model.AdminSession;
import it.unical.informatica.progettouid.model.ClientSession;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneHandlerAdmin {
    private static SceneHandlerAdmin instance = null;
    private Scene scene;
    private Stage stage;
    private BorderPane mainPane;

    private SceneHandlerAdmin() {}

    public static SceneHandlerAdmin getInstance() {
        if (instance == null) {
            instance = new SceneHandlerAdmin();
        }
        return instance;
    }

    public void init(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        this.mainPane = new BorderPane();
        this.scene = new Scene(mainPane, 1000, 600);
        this.stage.setScene(this.scene);
        this.stage.setTitle("FlexFit");
        loadView("/fxml/admin/dashboardAdmin.fxml");
        this.stage.show();
    }

    private void loadView(String fxmlPath) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource(fxmlPath));
            BorderPane newView = loader.load();
            // Sostituisce l'intero contenuto con il nuovo BorderPane
            this.mainPane.setCenter(newView);
        } catch (IOException e) {
            System.err.println("Errore nel caricamento della vista: " + fxmlPath);
            throw e;
        }
    }

    public void setDashboardView() throws Exception {
        loadView("/fxml/admin/dashboardAdmin.fxml");
    }

    public void setCheckIn() throws Exception {
        loadView("/fxml/admin/checkin.fxml");
    }

    public void setClient() throws Exception {
        loadView("/fxml/admin/membri.fxml");
    }

    public void setAddUser() throws Exception {
        loadView("/fxml/admin/aggiungiUtente.fxml");
    }

    public void setAddCourse() throws Exception {
        loadView("/fxml/admin/aggiungiCorso.fxml");
    }

    public void setBilling() throws Exception {
        loadView("/fxml/admin/billing.fxml");
    }

    public void setAccount() throws Exception {
        loadView("/fxml/admin/account.fxml"); // Modificato per puntare al file corretto
    }

    public void setSettings() throws Exception {
        loadView("/fxml/admin/settings.fxml"); // Modificato per puntare al file corretto
    }
    // Metodo per gestire il logout
    public void logout() throws Exception {
        try{
            AdminSession.getInstance().logout();

            if(mainPane!=null){
                mainPane.getChildren().clear();
            }

            SceneHandlerPrimaPagina handler = SceneHandlerPrimaPagina.getInstance();
            handler.init(this.stage);
        } catch (Exception e) {
            System.err.println(STR."Errore durante il logout: \{e.getMessage()}");
            throw new RuntimeException("Impossibile completare il logout", e);
        }
    }

    public void setAddPT() throws Exception {
        loadView("/fxml/admin/aggiungiPersonalTrainer.fxml");
    }
}
