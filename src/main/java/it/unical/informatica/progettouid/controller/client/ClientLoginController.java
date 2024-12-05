package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.Client;
import it.unical.informatica.progettouid.model.ClientData;
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
import org.springframework.security.core.userdetails.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientLoginController {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
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
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            //controlloLabel.setVisible(true);

            //controlloLabel.setText("Inserisci sia username che password.");
            return;
        }

        Task<ClientData> task = DBConnection.getInstance().authenticateUser(email, password);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(event -> {
            ClientData client = task.getValue();
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



        executorService.submit(task);
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
