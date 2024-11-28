package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.Client;
import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.model.SessionManager;
import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;


public class ClientLoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button backButton;

    @FXML
    private Hyperlink registerLink;

    @FXML
    public void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR,
                    "Errore di Login",
                    "Campi mancanti",
                    "Per favore compila tutti i campi.");
            return;

            // TODO: aggiungere una label nei file fxml per ricordare all0utente in caso non abbia completato i campi
            /* verifierText.setVisible(true);
            verifierText.setText("Si prega di completare i campi obbligatori (*).");*/
        }

        // TODO: Implementare la logica di autenticazione

        Task<Client> task = DBConnection.getInstance().authenticateUser(email, password);
        task.setOnSucceeded(event -> {
            Client client = task.getValue();
            if (client != null) {
                try {
                    SessionManager.getInstance().setLoggedClient(client);
                    SceneHandlerClient.getInstance().setDashboardView();
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        //controlloLabel.setVisible(true);
                        //controlloLabel.setText("Errore nell'accesso alla home.");
                    });
                }
            } else {
                Platform.runLater(() -> {
                    //controlloLabel.setVisible(true);
                    //controlloLabel.setText("Credenziali non valide.");
                });
            }
        });

        task.setOnFailed(event -> {
            Platform.runLater(() -> {
                //controlloLabel.setVisible(true);
                //controlloLabel.setText("Errore durante la verifica delle credenziali.");
            });
        });
        //executorService.submit(task);
    }

    @FXML
    public void handleBack() {
        try {
            SceneHandlerPrimaPagina.getInstance().init(SceneHandlerPrimaPagina.getInstance().getStage());
        } catch (Exception e) {
            showAlert(AlertType.ERROR,
                    "Errore di Navigazione",
                    "Impossibile tornare indietro",
                    "Si è verificato un errore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRegister() {
        // TODO: Implementare la navigazione alla schermata di registrazione


        showAlert(AlertType.INFORMATION,
                "Registrazione",
                "Funzionalità in arrivo",
                "La registrazione sarà disponibile a breve.");
    }


    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
