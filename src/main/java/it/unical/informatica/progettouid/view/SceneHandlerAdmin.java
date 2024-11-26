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
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/admin/adminDashboard.fxml"));
        Parent root = loader.load();
        this.mainPane = (BorderPane) root;
        this.scene = new Scene(root, 1000, 600);
        this.stage.setScene(scene);
        this.stage.setWidth(1000);
        this.stage.setHeight(600);
        this.stage.show();
    }

    public void loadDashboard(String username) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/adminDashboard.fxml"));
        Node view = loader.load();
        mainPane.setCenter(view);
    }

    public void setDashboardView() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/admin/adminDashboard.fxml"));
        Node view = loader.load();
        mainPane.setCenter(view);
    }

    public void setCheckIn() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/admin/checkin.fxml"));
        Node view = loader.load();
        mainPane.setCenter(view);
    }
    public void setClient() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/admin/membri.fxml"));
        Node view = loader.load();
        mainPane.setCenter(view);
    }
    // da modificare con personal trainer fxml
    public void setPersonalTrainer() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/admin/checkin.fxml"));
        Node view = loader.load();
        mainPane.setCenter(view);
    }
    public void setAddUser() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/admin/aggiungiUtente.fxml"));
        Node view = loader.load();
        mainPane.setCenter(view);
    }

    // da modificare con aggiungi corso FXML
    public void setAddCourse() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/admin/aggiungiUtente.fxml"));
        Node view = loader.load();
        mainPane.setCenter(view);
    }
    // da modificare con aggiunti PT fxml
    public void setAddPersonalTrainer() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/admin/adminDashboard.fxml"));
        Node view = loader.load();
        mainPane.setCenter(view);
    }
    public void setBilling() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/admin/billing.fxml"));
        Node view = loader.load();
        mainPane.setCenter(view);
    }

    // da aggiungere con account
    public void setAccount() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/admin/adminDashboard.fxml"));
        Node view = loader.load();
        mainPane.setCenter(view);
    }
    // modificare con impostazioni
    public void setSettings() throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/admin/adminDashboard.fxml"));
        Node view = loader.load();
        mainPane.setCenter(view);
    }


}
