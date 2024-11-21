package it.unical.informatica.progettouid.view;

import it.unical.informatica.progettouid.FlexFit;
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
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/client/dashboardClient.fxml"));
        Parent root = loader.load();
        this.mainPane = (BorderPane) root;
        this.scene = new Scene(root, 1000, 600);
        this.stage.setScene(scene);
        this.stage.setWidth(1000);
        this.stage.setHeight(600);
        this.stage.show();
    }


    public void setDashboardView() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/client/dashboardClient.fxml"));
        Node view = loader.load();
        if (view instanceof BorderPane) {
            BorderPane loadedBorderPane = (BorderPane) view;
            mainPane.setCenter(loadedBorderPane.getCenter());

            if (loadedBorderPane.getRight() != null) {
                mainPane.setRight(loadedBorderPane.getRight());
            } else {
                mainPane.setRight(null);
            }
        } else {
            mainPane.setCenter(view);
            mainPane.setRight(null);
        }
    }

    public void setAttivitaView() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/client/attivitaClient.fxml"));
        Node view = loader.load();
        if (view instanceof BorderPane) {
            BorderPane loadedBorderPane = (BorderPane) view;
            mainPane.setCenter(loadedBorderPane.getCenter());

            if (loadedBorderPane.getRight() != null) {
                mainPane.setRight(loadedBorderPane.getRight());
            } else {
                mainPane.setRight(null);
            }
        } else {
            mainPane.setCenter(view);
            mainPane.setRight(null);
        }
    }

    public void setPrenotazioniView() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/client/prenotazionePT.fxml"));
        Node view = loader.load();
        if (view instanceof BorderPane) {
            BorderPane loadedBorderPane = (BorderPane) view;
            mainPane.setCenter(loadedBorderPane.getCenter());

            if (loadedBorderPane.getRight() != null) {
                mainPane.setRight(loadedBorderPane.getRight());
            } else {
                mainPane.setRight(null);
            }
        } else {
            mainPane.setCenter(view);
            mainPane.setRight(null);
        }
    }

    public void setSchedaView() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/client/schedaClient.fxml"));
        Node view = loader.load();
        if (view instanceof BorderPane) {
            BorderPane loadedBorderPane = (BorderPane) view;
            mainPane.setCenter(loadedBorderPane.getCenter());

            if (loadedBorderPane.getRight() != null) {
                mainPane.setRight(loadedBorderPane.getRight());
            } else {
                mainPane.setRight(null);
            }
        } else {
            mainPane.setCenter(view);
            mainPane.setRight(null);
        }
    }
}
