package it.unical.informatica.progettouid.controller.Admin;

import it.unical.informatica.progettouid.view.SceneHandlerAdmin;
import javafx.fxml.FXML;

public class BillingController {

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
