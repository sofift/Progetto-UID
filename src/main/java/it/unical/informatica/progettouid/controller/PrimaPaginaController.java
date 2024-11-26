package it.unical.informatica.progettouid.controller;

import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PrimaPaginaController {
    @FXML
    private Button btnClient;
    @FXML
    private Button btnAdmin;
    @FXML
    private Button btnTrainer;

    @FXML
    public void onClientLogin() {
        handleUserSelection(SceneHandlerPrimaPagina.UserType.CLIENT);
    }

    @FXML
    public void onAdminLogin() {
        handleUserSelection(SceneHandlerPrimaPagina.UserType.ADMIN);
    }

    @FXML
    public void onTrainerLogin() {
        handleUserSelection(SceneHandlerPrimaPagina.UserType.TRAINER);
    }


    private void handleUserSelection(SceneHandlerPrimaPagina.UserType userType) {
        try {
            SceneHandlerPrimaPagina.getInstance().loadLoginScreen(userType);
        } catch (Exception e) {
            showErrorAlert("Errore di Caricamento",
                    "Impossibile caricare la schermata di login",
                    "Si è verificato un errore durante il caricamento della schermata di login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
