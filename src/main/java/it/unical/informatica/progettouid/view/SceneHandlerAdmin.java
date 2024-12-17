package it.unical.informatica.progettouid.view;

import it.unical.informatica.progettouid.FlexFit;
import it.unical.informatica.progettouid.model.AdminSession;
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

    private void loadView(String fxmlPath) throws Exception {
        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource(fxmlPath));
        Node view = loader.load();
        this.mainPane = (BorderPane) view;
        this.scene = new Scene(mainPane, 1000, 600);
        this.stage.setScene(scene);
    }

    public void setDashboardView() throws Exception{
        loadView("/fxml/admin/dashboardAdmin.fxml");
    }

    public void setCheckInView() throws Exception{
        loadView("/fxml/admin/checkIn.fxml");
    }

    public void setClientView () throws Exception {
        loadView("fxml/admin/aggiungiUtente.fxml");
    }

    // da aggiungere collegamenti con fxml: addPT, addCourse

    public void setBillingView() throws Exception {
        loadView("/fxml/admin/billing.fxml");

    }

    // da aggiungere:  account e impostazioni


}
