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
    private Stage mainStage;
    private Stage loginStage;
    private BorderPane mainPane;


    private SceneHandlerClient() {}

    public static SceneHandlerClient getInstance() {
        if (instance == null) {
            instance = new SceneHandlerClient();
        }
        return instance;
    }

    public void init(Stage loginStage) throws Exception {
        this.loginStage = loginStage;
        loadLoginView();
        this.loginStage.setWidth(600);
        this.loginStage.setHeight(400);
        this.loginStage.show();
    }

    private void loadLoginView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/clientLogin.fxml"));
        Scene loginScene = new Scene(loader.load());
        loginStage.setScene(loginScene);
    }

    private void loadView(String fxmlPath) throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource(fxmlPath));
        BorderPane newContent = loader.load();

        // Aggiorna solo le sezioni necessarie
        if (newContent.getTop() != null) mainPane.setTop(newContent.getTop());
        if (newContent.getCenter() != null) mainPane.setCenter(newContent.getCenter());
        if (newContent.getRight() != null) mainPane.setRight(newContent.getRight());


        /*Parent view = loader.load();
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
        }*/
    }

    public void setDashboardView() throws IOException {
        mainStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/dashboardClient.fxml"));
        mainPane = loader.load();
        this.scene = new Scene(mainPane);
        scene.getStylesheets().add(getClass().getResource("/styles/home.css").toExternalForm());

        mainStage.setScene(scene);
        mainStage.setWidth(1000);
        mainStage.setHeight(600);

        loginStage.close();
        mainStage.show();
    }

    /*public void setDashboardView() throws Exception {
        loadView("/fxml/client/dashboardClient.fxml", false);
    }*/

    public void setAttivitaView() throws Exception {
        loadView("/fxml/client/attivitaClient.fxml");
    }

    public void setPrenotazioniView() throws Exception {
        loadView("/fxml/client/prenotazionePT.fxml");
    }

    public void setSchedaView() throws Exception {
        loadView("/fxml/client/schedaClient.fxml");
    }

    // Metodo per gestire il logout
    public void logout() throws Exception {
        // Pulisci la sessione
        SessionManager.getInstance().clearSession();
        // Torna alla schermata di login
        //loadView("/fxml/login.fxml", true);
    }

    public void setAbbonamentoView() throws Exception {
        loadView("/fxml/client/abbonamentoClient.fxml");
    }
}
