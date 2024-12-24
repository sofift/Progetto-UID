package it.unical.informatica.progettouid.controller.Trainer;

import it.unical.informatica.progettouid.model.*;
import it.unical.informatica.progettouid.view.AlertManager;
import it.unical.informatica.progettouid.view.SceneHandlerPT;
import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrainerLoginController {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label controlloAccessoLabel;

    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            controlloAccessoLabel.setVisible(true);
            controlloAccessoLabel.setText("Campi mancanti");
            return;
        }

        Task<PersonalTrainer> task = DBConnection.getInstance().authenticateTrainer(email, password);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(event -> {
            PersonalTrainer trainer = task.getValue();
            if (trainer != null) {
                try {
                    PTSession.getInstance().setCurrentTrainer(trainer);
                    SceneHandlerPrimaPagina.getInstance().switchToTrainerView();
                    SceneHandlerPT.getInstance().setDashboardView();
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
            AlertManager err = new AlertManager(Alert.AlertType.ERROR, "Errore di navigazione", "Impossibile tornare indietro", STR."Si è verificato un errore: \{e.getMessage()}");
            e.printStackTrace();
        }
    }




}
