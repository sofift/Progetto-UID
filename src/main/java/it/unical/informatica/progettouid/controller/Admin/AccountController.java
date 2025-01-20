package it.unical.informatica.progettouid.controller.Admin;

import it.unical.informatica.progettouid.model.Admin;
import it.unical.informatica.progettouid.model.AdminSession;
import it.unical.informatica.progettouid.view.SceneHandlerAdmin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class AccountController implements Initializable {
    @FXML private Label usernameLabel;
    @FXML private Label nomeLabel;
    @FXML private Label cognomeLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAdminInfo();
    }

    private void loadAdminInfo() {
        Admin currentAdmin = AdminSession.getInstance().getCurrentAdmin();
        if (currentAdmin != null) {
            usernameLabel.setText(currentAdmin.email());
            nomeLabel.setText(currentAdmin.nome());
            cognomeLabel.setText(currentAdmin.cognome());
        }
    }

    @FXML
    private void navigateToHome() throws Exception {
        SceneHandlerAdmin.getInstance().setDashboardView();
    }

    @FXML
    private void navigateToCheckIn() throws Exception {
        SceneHandlerAdmin.getInstance().setCheckIn();
    }

    @FXML
    private void navigateToMembers() throws Exception {
        SceneHandlerAdmin.getInstance().setClient();
    }

    @FXML
    private void navigateToAddUser() throws Exception {
        SceneHandlerAdmin.getInstance().setAddUser();
    }

    @FXML
    private void navigateToAddPT() throws Exception {
        SceneHandlerAdmin.getInstance().setAddPT();
    }

    @FXML
    private void navigateToAddCourse() throws Exception {
        SceneHandlerAdmin.getInstance().setAddCourse();
    }

    @FXML
    private void navigateToBilling() throws Exception {
        SceneHandlerAdmin.getInstance().setBilling();
    }

    @FXML
    private void navigateToAccount() throws Exception {
        SceneHandlerAdmin.getInstance().setAccount();
    }

    @FXML
    private void navigateToSettings() throws Exception {
        SceneHandlerAdmin.getInstance().setSettings();
    }
}