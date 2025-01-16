package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.Client;
import it.unical.informatica.progettouid.model.ClientSession;
import it.unical.informatica.progettouid.view.AlertManager;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    public void initialize() {
        client = ClientSession.getInstance().getCurrentClient();
        nomeLabel.setText("Nome: " + client.nome() );
        cognomeLabel.setText("Cognome: " + client.cognome() );
        dataNascitaLabel.setText("Data di nascita: " + client.dataNascita());
        emailLabel.setText("Email: " + client.email() );
    }

    @FXML
    public void onNavigationButtonClick(ActionEvent event) {
        Button button = (Button) event.getSource();
        try {
            switch (button.getId()) {
                case "dashboardClient":
                    SceneHandlerClient.getInstance().setDashboardView();
                    break;
                case "attivitaClient":
                    SceneHandlerClient.getInstance().setAttivitaView();
                    break;
                case "prenotazionePT":
                    SceneHandlerClient.getInstance().setPrenotazioniView();
                    break;
                case "schedaClient":
                    SceneHandlerClient.getInstance().setSchedaView();
                    break;
                case "abbonamentoClient":
                    SceneHandlerClient.getInstance().setAbbonamentoView();
                    break;
                case "impostazioniClient":
                    SceneHandlerClient.getInstance().setImpostazioniView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout(ActionEvent actionEvent) throws Exception {
        try {
            SceneHandlerClient.getInstance().logout();
        } catch (Exception e) {
            AlertManager al = new AlertManager(Alert.AlertType.ERROR, "Errore", "Errore durante il logout", "Si è verificato un errore durante il logout. Riprova");
            al.showAndWait();
        }}
}
