package it.unical.informatica.progettouid.controller.Admin;

import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.view.SceneHandlerAdmin;
import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

public class AdminLoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button backButton;

    @FXML
    private VBox loginForm;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    public void initialize() {
        progressIndicator.setVisible(false);

        // Aggiunge listener per abilitare/disabilitare il pulsante di login
        loginButton.disableProperty().bind(
                usernameField.textProperty().isEmpty()
                        .or(passwordField.textProperty().isEmpty())
        );
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Mostra l'indicatore di caricamento
        loginForm.setDisable(true);
        progressIndicator.setVisible(true);

        var task = DBConnection.getInstance().validateAdminCredentials(username, password);

        task.setOnSucceeded(event -> {
            progressIndicator.setVisible(false);
            loginForm.setDisable(false);

            Boolean isValid = task.getValue();
            if (isValid) {
                navigateToAdminDashboard(username);
            } else {
                showAlert(AlertType.ERROR,
                        "Errore di Login",
                        "Credenziali non valide",
                        "Username o password non corretti.");
            }
        });

        task.setOnFailed(event -> {
            progressIndicator.setVisible(false);
            loginForm.setDisable(false);

            showAlert(AlertType.ERROR,
                    "Errore di Sistema",
                    "Errore durante il login",
                    "Si è verificato un errore durante l'autenticazione: " +
                            task.getException().getMessage());
            task.getException().printStackTrace();
        });

        // Esegui il task in background
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void navigateToAdminDashboard(String username) {
        try {
            Platform.runLater(() -> {
                try {
                    SceneHandlerAdmin.getInstance().loadDashboard(username);
                } catch (Exception e) {
                    showAlert(AlertType.ERROR,
                            "Errore di Navigazione",
                            "Impossibile caricare la dashboard",
                            "Si è verificato un errore: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            showAlert(AlertType.ERROR,
                    "Errore di Sistema",
                    "Errore durante la navigazione",
                    "Si è verificato un errore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack() {
        try {
            SceneHandlerPrimaPagina.getInstance().init(
                    SceneHandlerPrimaPagina.getInstance().getStage());
        } catch (Exception e) {
            showAlert(AlertType.ERROR,
                    "Errore di Navigazione",
                    "Impossibile tornare indietro",
                    "Si è verificato un errore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}
