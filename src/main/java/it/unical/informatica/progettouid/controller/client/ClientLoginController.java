package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
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
        }

        // TODO: Implementare la logica di autenticazione
        // Per ora simuliamo un login di successo
        try {
            // Qui andrebbe la verifica delle credenziali
            if (validateCredentials(email, password)) {
                SceneHandlerClient.getInstance().init(SceneHandlerPrimaPagina.getInstance().getStage());
            } else {
                showAlert(AlertType.ERROR,
                        "Errore di Login",
                        "Credenziali non valide",
                        "Email o password non corrette.");
            }
        } catch (Exception e) {
            showAlert(AlertType.ERROR,
                    "Errore di Sistema",
                    "Errore durante il login",
                    "Si è verificato un errore: " + e.getMessage());
            e.printStackTrace();
        }
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

    private boolean validateCredentials(String email, String password) {
        // TODO: Implementare la vera validazione delle credenziali
        // Per ora ritorna true per simulare un login di successo
        return true;
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
