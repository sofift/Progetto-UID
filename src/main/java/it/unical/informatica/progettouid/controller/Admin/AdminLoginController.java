package it.unical.informatica.progettouid.controller.Admin;

import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AdminLoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button backButton;
    @FXML private VBox loginForm;
    //@FXML private ProgressIndicator progressIndicator;

    @FXML
    public void initialize() {
        //progressIndicator.setVisible(false);

        // Abilita/disabilita il pulsante di login
        loginButton.disableProperty().bind(
                usernameField.textProperty().isEmpty()
                        .or(passwordField.textProperty().isEmpty())
        );

        // Aggiungi listener per il tasto Invio
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER") && !loginButton.isDisabled()) {
                handleLogin();
            }
        });
    }

    private boolean validateCredentials(String username, String password) {
        try {
            // Qui inserire la logica di validazione con il database
            return DBConnection.getInstance().validateAdminCredentials(username, password);
        } catch (Exception e) {
            System.err.println("Errore durante la validazione delle credenziali: " + e.getMessage());
            return false;
        }
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        //progressIndicator.setVisible(true);
        loginForm.setDisable(true);

        // Esegui la validazione in un thread separato
        new Thread(() -> {
            try {
                if (validateCredentials(username, password)) {
                    Platform.runLater(() -> {
                        try {
                            SceneHandlerPrimaPagina.getInstance()
                                    .loadMainInterface(SceneHandlerPrimaPagina.UserType.ADMIN);
                        } catch (Exception e) {
                            showAlert(Alert.AlertType.ERROR,
                                    "Errore di Navigazione",
                                    "Impossibile caricare la dashboard",
                                    "Si è verificato un errore: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        showAlert(Alert.AlertType.ERROR,
                                "Errore di Login",
                                "Credenziali non valide",
                                "Username o password non corrette.");
                        passwordField.clear();
                    });
                }
            } finally {
                Platform.runLater(() -> {
                    //progressIndicator.setVisible(false);
                    loginForm.setDisable(false);
                });
            }
        }).start();
    }

    @FXML
    public void handleBack() {
        try {
            SceneHandlerPrimaPagina.getInstance().loadPrimaPagina();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Errore di Navigazione",
                    "Impossibile tornare indietro",
                    "Si è verificato un errore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}