package it.unical.informatica.progettouid.view;

import it.unical.informatica.progettouid.FlexFit;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SceneHandlerAdmin {
    private static SceneHandlerAdmin instance = null;
    private Scene scene;
    private Stage stage;
    private BorderPane mainPane;
    //private static final String FXML_PATH = "/fxml/admin/";

    private SceneHandlerAdmin() {}

    public static SceneHandlerAdmin getInstance() {
        if (instance == null) {
            instance = new SceneHandlerAdmin();
        }
        return instance;
    }

    public void init(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/admin/dashboardAdmin.fxml"));
        this.scene = new Scene((Parent)loader.load(), 1000, 600);
        this.stage.setScene(this.scene);
        this.stage.setTitle("FlexFit");
        this.stage.show();
    }

    private void loadView(String fxmlPath) throws Exception{
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource(fxmlPath));
        Node view = loader.load();
        this.mainPane = (BorderPane) view;
        this.scene = new Scene(mainPane, 1000, 600);
        this.stage.setScene(scene);
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

    // da modificare con aggiungi corso FXML
    public void setAddCourse() throws Exception {
        loadView("/fxml/admin/aggiungiCorso.fxml");
    }
    public void setBilling() throws Exception {
        loadView("/fxml/admin/billing.fxml");
    }

    // da aggiungere con account
    public void setAccount() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/admin/dashboardAdmin.fxml"));
        Node view = loader.load();
        mainPane.setCenter(view);
    }
    // modificare con impostazioni
    public void setSettings() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/admin/dashboardAdmin.fxml"));
        Node view = loader.load();
        mainPane.setCenter(view);
    }


}
