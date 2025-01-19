package it.unical.informatica.progettouid.view;

import it.unical.informatica.progettouid.FlexFit;
import it.unical.informatica.progettouid.model.PTSession;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


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
        if (this.stage == null) {
            this.stage = primaryStage;
            this.scene = new Scene(loadFXML("/fxml/pt/dashboardTrainer.fxml"), 1000, 400);
            this.stage.setTitle("CoWork Manager");
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

    public void setDashboardView() throws Exception {
        if(this.scene != null)
            this.scene.setRoot(loadFXML("/fxml/pt/dashboardTrainer.fxml"));
    }

    public void setArticoliView() throws Exception {
        if(this.scene != null)
            this.scene.setRoot(loadFXML("/fxml/pt/articoli.fxml"));
    }

    public void setCreazioneSchedaView() throws Exception {
        if(this.scene != null)
            this.scene.setRoot(loadFXML("/fxml/pt/creazioneScheda.fxml"));
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
            this.scene.setRoot(loadFXML("/fxml/pt/impostazioniPT.fxml"));
    }

}
