package it.unical.informatica.progettouid.view;

import it.unical.informatica.progettouid.FlexFit;
import it.unical.informatica.progettouid.model.ClientSession;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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
        FXMLLoader fxmlLoader = new FXMLLoader(FlexFit.class.getResource("/fxml/client/dashboardClient.fxml"));
        this.scene = new Scene((Parent)fxmlLoader.load(), 1000, 600);
        this.stage.setScene(this.scene);
        this.stage.setTitle("FlexFit");
        this.stage.show();
    }

    private void loadView(String fxmlPath) throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource(fxmlPath));
        Node view = loader.load();
        this.mainPane = (BorderPane) view;
        this.scene = new Scene(mainPane, 1000, 600);
        this.stage.setScene(scene);
        /*} else {
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
            }*/

    }

    public void setRegistrazioneView() throws Exception {
        Stage registrazioneStage = new Stage(); // Crea un nuovo Stage
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/registrazione.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        registrazioneStage.setScene(scene);
        registrazioneStage.setTitle("Registrazione");
        registrazioneStage.initOwner(stage); // Imposta il proprietario per evitare problemi di focus
        registrazioneStage.initModality(Modality.APPLICATION_MODAL); // Impedisce interazioni con la finestra principale finché è aperta
        registrazioneStage.show();
    }

    public void setDashboardView() throws Exception {
        loadView("/fxml/client/dashboardClient.fxml");
    }

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
        ClientSession.getInstance().logout();
        // Torna alla schermata di login
        //loadView("/fxml/login.fxml", true);
    }

    public void setAbbonamentoView() throws Exception {
        loadView("/fxml/client/abbonamentoClient.fxml");
    }
}
