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
        FXMLLoader fxmlLoader = new FXMLLoader(FlexFit.class.getResource("/fxml/pt/dashboardTrainer.fxml"));
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
        // Pulisci la sessione
        PTSession.getInstance().logout();
        // Torna alla schermata di login
        //loadView("/fxml/login.fxml", true);
    }

}
