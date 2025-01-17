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


    private SceneHandlerClient() {}

    public static SceneHandlerClient getInstance() {
        if (instance == null) {
            instance = new SceneHandlerClient();
        }
        return instance;
    }

    public void init(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        this.scene = new Scene(loadView("/fxml/client/dashboardClient.fxml"), 1000, 600);
        this.stage.setScene(this.scene);
        this.stage.setTitle("FlexFit");
        this.stage.show();
    }

    private <T> T loadView(String fxmlPath) throws Exception {
        try{
            FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource(fxmlPath));
            return loader.load();
        } catch (IOException e) {
            System.err.println(STR."Errore nel caricamento della vista: \{fxmlPath}");
            throw e;

        }

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
        if(this.scene != null)
            this.scene.setRoot(loadView("/fxml/client/dashboardClient.fxml"));
    }

    public void setAttivitaView() throws Exception {
        if(this.scene != null)
            this.scene.setRoot(loadView("/fxml/client/attivitaClient.fxml"));
    }

    public void setPrenotazioniView() throws Exception {
        if(this.scene != null)
            this.scene.setRoot(loadView("/fxml/client/prenotazionePT.fxml"));
    }

    public void setSchedaView() throws Exception {
        if(this.scene != null)
            this.scene.setRoot(loadView("/fxml/client/schedaClient.fxml"));
    }

    // Metodo per gestire il logout
    public void logout() throws Exception {
        try{
            ClientSession.getInstance().logout();

            SceneHandlerPrimaPagina handler = SceneHandlerPrimaPagina.getInstance();
            handler.init(this.stage);
        } catch (Exception e) {
            System.err.println(STR."Errore durante il logout: \{e.getMessage()}");
            throw new RuntimeException("Impossibile completare il logout", e);
        }
    }

    public void setAbbonamentoView() throws Exception {
        if(this.scene != null)
            this.scene.setRoot(loadView("/fxml/client/abbonamentoClient.fxml"));
    }

    public void setImpostazioniView() throws Exception {
        if(this.scene != null)
            this.scene.setRoot(loadView("/fxml/client/impostazioniClient.fxml"));
    }
}
