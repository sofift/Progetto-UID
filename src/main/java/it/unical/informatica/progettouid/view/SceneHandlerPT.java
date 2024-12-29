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
        this.mainPane = new BorderPane();
        this.scene = new Scene(mainPane, 1000, 600);
        this.stage.setScene(this.scene);
        this.stage.setTitle("FlexFit");
        loadView("/fxml/pt/dashboardTrainer.fxml");
        this.stage.show();
    }

    private void loadView(String fxmlPath) throws Exception {
        try{
            FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource(fxmlPath));
            BorderPane newView = loader.load();
            // Sostituisce l'intero contenuto con il nuovo BorderPane
            this.mainPane.setCenter(newView);
        } catch (IOException e) {
            System.err.println(STR."Errore nel caricamento della vista: \{fxmlPath}");
            throw e;

        }

    }

    public void setDashboardView() throws Exception {
        loadView("/fxml/pt/dashboardTrainer.fxml");
    }

    public void setAttivitaPTView() throws Exception {
        loadView("/fxml/pt/attivitaPT.fxml");
    }

    public void setCreazioneSchedaView() throws Exception {
        loadView("/fxml/pt/creazioneScheda.fxml");
    }

    // Metodo per gestire il logout
    public void logout() throws Exception {
        try{
            PTSession.getInstance().logout();

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

}
