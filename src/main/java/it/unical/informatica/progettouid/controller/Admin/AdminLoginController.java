package it.unical.informatica.progettouid.controller.Admin;

import it.unical.informatica.progettouid.model.Admin;
import it.unical.informatica.progettouid.model.AdminSession;
import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.view.SceneHandlerAdmin;
import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminLoginController {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label checkAccess;

    @FXML
    public void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            checkAccess.setVisible(true);
            checkAccess.setText("Campi mancanti");
            return;
        }

        Task<Admin> task = DBConnection.getInstance().authenticateAdmin(email, password);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(event -> {
            Admin admin = task.getValue();
            if (admin != null){
                try{
                    AdminSession.getInstance().setCurrentAdmin(admin);
                    SceneHandlerPrimaPagina.getInstance().switchToAdminView();
                    SceneHandlerAdmin.getInstance().setDashboardView();
                } catch (Exception e){
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        checkAccess.setVisible(true);
                        checkAccess.setText("Errore nell'accesso alla home.");
                    });
                }
            } else {
                Platform.runLater(() -> {
                    checkAccess.setVisible(true);
                    checkAccess.setText("Credenziali non valide.");
                });
            }
        });

        task.setOnFailed(event -> {
            Platform.runLater(() -> {
                checkAccess.setVisible(true);
                checkAccess.setText("Errore durante la verifica delle credenziali.");
            });
        });

        executorService.submit(task);

    }

    //@FXML
    public void handleBack() {
        try {
            SceneHandlerPrimaPagina.getInstance().init(SceneHandlerPrimaPagina.getInstance().getStage());
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