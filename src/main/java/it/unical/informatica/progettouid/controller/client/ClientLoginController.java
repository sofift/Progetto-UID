package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.Client;
import it.unical.informatica.progettouid.model.ClientSession;
import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.model.User;
import it.unical.informatica.progettouid.view.AlertManager;
import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientLoginController {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label controlloAccessoLabel;

    @FXML
    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            controlloAccessoLabel.setVisible(true);
            controlloAccessoLabel.setText("Campi mancanti");
            return;
        }

        Task<Client> task = DBConnection.getInstance().authenticateUser(email, password);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(event -> {
            Client client = task.getValue();
            if (client != null) {
                try {
                    ClientSession.getInstance().setCurrentClient(client);
                    SceneHandlerPrimaPagina.getInstance().switchToClientView();
                    SceneHandlerClient.getInstance().setDashboardView();
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        controlloAccessoLabel.setVisible(true);
                        controlloAccessoLabel.setText("Errore nell'accesso alla home.");
                    });
                }
            } else {
                Platform.runLater(() -> {
                    controlloAccessoLabel.setVisible(true);
                    controlloAccessoLabel.setText("Credenziali non valide.");
                });
            }
        });

        task.setOnFailed(event -> {
            Platform.runLater(() -> {
                controlloAccessoLabel.setVisible(true);
                controlloAccessoLabel.setText("Errore durante la verifica delle credenziali.");
            });
        });



        executorService.submit(task);
    }

    @FXML
    public void handleBack() {
        try {
            SceneHandlerPrimaPagina.getInstance().init(SceneHandlerPrimaPagina.getInstance().getStage());
        } catch (Exception e) {
            AlertManager errore = new AlertManager(AlertType.ERROR, "Errore di navigazione","Impossibile tornare indietro", STR."Si è verificato un errore: \{e.getMessage()}");
            errore.display();
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRegister() throws Exception {
        SceneHandlerClient.getInstance().setRegistrazioneView();


    }



}
