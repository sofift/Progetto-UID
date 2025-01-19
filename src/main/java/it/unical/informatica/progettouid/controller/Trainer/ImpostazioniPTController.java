package it.unical.informatica.progettouid.controller.Trainer;

import it.unical.informatica.progettouid.model.Client;
import it.unical.informatica.progettouid.model.ClientSession;
import it.unical.informatica.progettouid.model.PTSession;
import it.unical.informatica.progettouid.model.PersonalTrainer;
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
    @FXML
    private Label dataNascitaLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Label nomeLabel;
    @FXML
    private PasswordField passwordField;
    private PersonalTrainer personal = null;

    void initialize() {
        personal = PTSession.getInstance().getCurrentTrainer();
        nomeLabel.setText(STR."Nome: \{personal.nome()}");
        cognomeLabel.setText(STR."Cognome: \{personal.cognome()}");
        dataNascitaLabel.setText(STR."Data di nascita: \{personal.dataNascita()}");
        emailLabel.setText(STR."Email: \{personal.email()}");
    }


    @FXML
    public void logout(ActionEvent actionEvent) throws Exception {
        try {
            SceneHandlerPT.getInstance().logout();
        } catch (Exception e) {
            AlertManager al = new AlertManager(Alert.AlertType.ERROR, "Errore", "Errore durante il logout", "Si è verificato un errore durante il logout. Riprova");
            al.showAndWait();
        }
    }

    @FXML
    public void onNavigationButtonClick(ActionEvent event) {
        Button button = (Button) event.getSource();
        try {
            switch (button.getId()) {
                case "dashboardTrainer":
                    SceneHandlerPT.getInstance().setDashboardView();
                    break;
                case "attivitaPT":
                    SceneHandlerPT.getInstance().setAttivitaPTView();
                    break;
                case "creazioneScheda":
                    SceneHandlerPT.getInstance().setCreazioneSchedaView();
                    break;
                case "impostazioniPT":
                    SceneHandlerPT.getInstance().setImpostazioniView();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
