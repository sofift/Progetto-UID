package it.unical.informatica.progettouid.view;

import it.unical.informatica.progettouid.FlexFit;
import it.unical.informatica.progettouid.model.ClientSession;
import it.unical.informatica.progettouid.model.PTSession;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


public class SceneHandlerPT {
    private static SceneHandlerPT instance = null;
    private Scene scene;
    private Stage stage;
    private BorderPane mainPane;

    private SceneHandlerPT() {}

    public static SceneHandlerPT getInstance() {
        if (instance == null) {
            instance = new SceneHandlerPT();
        }
        return instance;
    }

    public void init(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        this.scene = new Scene(loadView("/fxml/pt/dashboardTrainer.fxml"), 1000, 600);
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

    public void setDashboardView() throws Exception {
        if(this.scene != null)
            this.scene.setRoot(loadView("/fxml/pt/dashboardTrainer.fxml"));
    }

    public void setAttivitaPTView() throws Exception {
        if(this.scene != null)
            this.scene.setRoot(loadView("/fxml/pt/attivitaPT.fxml"));
    }

    public void setCreazioneSchedaView() throws Exception {
        if(this.scene != null)
            this.scene.setRoot(loadView("/fxml/pt/creazioneScheda.fxml"));
    }

    // Metodo per gestire il logout
    public void logout() throws Exception {
        try{
            PTSession.getInstance().logout();

            SceneHandlerPrimaPagina handler = SceneHandlerPrimaPagina.getInstance();
            handler.init(this.stage);
        } catch (Exception e) {
            System.err.println(STR."Errore durante il logout: \{e.getMessage()}");
            throw new RuntimeException("Impossibile completare il logout", e);
        }
    }

    public void setImpostazioniView() throws Exception {
        if(this.scene != null)
            this.scene.setRoot(loadView("/fxml/client/impostazioniClient.fxml"));
    }

}
