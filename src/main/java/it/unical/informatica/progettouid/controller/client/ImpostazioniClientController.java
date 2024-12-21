package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.Client;
import it.unical.informatica.progettouid.model.ClientSession;
import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class ImpostazioniClientController {
    @FXML private Label cognomeLabel;
    @FXML private Label dataNascitaLabel;
    @FXML private Label emailLabel;
    @FXML private Button logoutButton;
    @FXML private Label nomeLabel;
    @FXML private PasswordField passwordField;
    private Client client = null;

    void initialize() {
        client = ClientSession.getInstance().getCurrentClient();
        nomeLabel.setText("Nome: " + client.getNome() );
        cognomeLabel.setText("Cognome: " + client.getCognome() );
        dataNascitaLabel.setText("Data di nascita: " + client.getDataNascita());
        emailLabel.setText("Email: " + client.getEmail() );
    }

    @FXML
    void onNavigationButtonClick(ActionEvent event) {

    }

    public void logout(ActionEvent actionEvent) throws Exception {
        ClientSession.getInstance().logout();
        SceneHandlerPrimaPagina.getInstance().loadPrimaPagina();
    }
}
