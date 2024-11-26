package it.unical.informatica.progettouid.view;

import it.unical.informatica.progettouid.FlexFit;
import it.unical.informatica.progettouid.model.SessionManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneHandlerClient {
    private static SceneHandlerClient instance = null;
    private Scene scene;
    private Stage stage;
    private BorderPane mainPane;


    private SceneHandlerClient() {}

    public static SceneHandlerClient getInstance() {
        if (instance == null) {
            instance = new SceneHandlerClient();
        }
        return instance;
    }

    public void init(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        loadView("/fxml/client/abbonamentoClient.fxml", true);
        this.stage.setWidth(1000);
        this.stage.setHeight(600);
        this.stage.show();
    }

    private void loadView(String fxmlPath, boolean isInitialLoad) throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource(fxmlPath));
        Node view = loader.load();
        if (isInitialLoad) {
            // Prima inizializzazione
            this.mainPane = (BorderPane) view;
            this.scene = new Scene(mainPane, 1000, 600);
            this.stage.setScene(scene);
        } else {
            // Aggiornamento vista esistente
            if (view instanceof BorderPane) {
                BorderPane loadedBorderPane = (BorderPane) view;
                mainPane.setCenter(loadedBorderPane.getCenter());
                mainPane.setRight(loadedBorderPane.getRight() != null ?
                        loadedBorderPane.getRight() : null);
                mainPane.setTop(loadedBorderPane.getTop());
            } else {
                mainPane.setCenter(view);
                mainPane.setRight(null);
            }
        }
    }

    public void setDashboardView() throws Exception {
        loadView("/fxml/client/dashboardClient.fxml", false);
    }

    public void setAttivitaView() throws Exception {
        loadView("/fxml/client/attivitaClient.fxml", false);
    }

    public void setPrenotazioniView() throws Exception {
        loadView("/fxml/client/prenotazionePT.fxml", false);
    }

    public void setSchedaView() throws Exception {
        loadView("/fxml/client/schedaClient.fxml", false);
    }

    // Metodo per gestire il logout
    public void logout() throws Exception {
        // Pulisci la sessione
        SessionManager.getInstance().clearSession();
        // Torna alla schermata di login
        //loadView("/fxml/login.fxml", true);
    }

    public void setAbbonamentoView() throws Exception {
        loadView("/fxml/client/abbonamentoClient.fxml", false);
    }
}
