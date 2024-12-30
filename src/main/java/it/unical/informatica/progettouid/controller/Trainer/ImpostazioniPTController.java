package it.unical.informatica.progettouid.controller.Trainer;

import it.unical.informatica.progettouid.model.Client;
import it.unical.informatica.progettouid.model.ClientSession;
import it.unical.informatica.progettouid.view.AlertManager;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import it.unical.informatica.progettouid.view.SceneHandlerPT;
import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class ImpostazioniPTController {
    @FXML
    private Label cognomeLabel;
    @FXML private Label dataNascitaLabel;
    @FXML private Label emailLabel;
    @FXML private Button logoutButton;
    @FXML private Label nomeLabel;
    @FXML private PasswordField passwordField;
    private Client client = null;

    void initialize() {
        client = ClientSession.getInstance().getCurrentClient();
        nomeLabel.setText(STR."Nome: \{client.nome()}");
        cognomeLabel.setText(STR."Cognome: \{client.cognome()}");
        dataNascitaLabel.setText(STR."Data di nascita: \{client.dataNascita()}");
        emailLabel.setText(STR."Email: \{client.email()}");
    }

    @FXML
    void onNavigationButtonClick(ActionEvent event) {

    }

    @FXML
    public void logout(ActionEvent actionEvent) throws Exception {
        try {
            SceneHandlerPT.getInstance().logout();
        } catch (Exception e) {
            AlertManager al = new AlertManager(Alert.AlertType.ERROR, "Errore", "Errore durante il logout", "Si è verificato un errore durante il logout. Riprova");
            al.showAndWait();
        }}
}
