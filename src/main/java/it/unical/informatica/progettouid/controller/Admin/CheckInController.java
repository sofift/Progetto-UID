package it.unical.informatica.progettouid.controller.Admin;

import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.view.SceneHandlerAdmin;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CheckInController {

    @FXML
    private Button btnCheckIn;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnEnter;
    @FXML
    private TextField displayField;
    @FXML
    private Label statusLabel;

    private final StringBuilder input = new StringBuilder();

    @FXML
    private void onNumberClick(javafx.event.ActionEvent event) {
        // Ottieni il numero dal bottone premuto
        String number = ((Button) event.getSource()).getText();
        input.append(number);
        displayField.setText(input.toString());
    }

    @FXML
    private void onClear() {
        // Resetta il campo di testo e la memoria
        input.setLength(0);
        displayField.clear();
        statusLabel.setText("");
    }

    @FXML
    private void onEnter() {
        // Conferma l'input e avvia il check-in
        if (input.length() > 0) {
            onSubmit();
        } else {
            statusLabel.setText("Inserire un codice valido.");
        }
    }

    @FXML
    private void onSubmit() {
        String code = displayField.getText();

        if (code.isEmpty()) {
            statusLabel.setText("Inserire un codice valido.");
            return;
        }

        Task<Boolean> task = DBConnection.getInstance().checkIn(code);

        task.setOnSucceeded(event -> {
            if (task.getValue()) {
                statusLabel.setText("Check-in registrato con successo.");
                onClear(); // Resetta il campo dopo il check-in
            } else {
                statusLabel.setText("Check-in fallito.");
            }
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            if (exception instanceof IllegalArgumentException) {
                statusLabel.setText(exception.getMessage());
            } else {
                statusLabel.setText("Errore durante il check-in");
                exception.printStackTrace();
            }
        });

        // Esegui il task in un thread separato
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void navigateToHome() throws Exception {
        SceneHandlerAdmin.getInstance().setDashboardView();
    }

    @FXML
    private void navigateToCheckIn() throws Exception {
        SceneHandlerAdmin.getInstance().setCheckIn();
    }

    @FXML
    private void navigateToMembers() throws Exception {
        SceneHandlerAdmin.getInstance().setClient();
    }

    @FXML
    private void navigateToAddUser() throws Exception {
        SceneHandlerAdmin.getInstance().setAddUser();
    }

    @FXML
    private void navigateToAddPT() throws Exception {
        SceneHandlerAdmin.getInstance().setAddPT();
    }

    @FXML
    private void navigateToAddCourse() throws Exception {
        SceneHandlerAdmin.getInstance().setAddCourse();
    }

    @FXML
    private void navigateToBilling() throws Exception {
        SceneHandlerAdmin.getInstance().setBilling();
    }

    @FXML
    private void navigateToAccount() throws Exception {
        SceneHandlerAdmin.getInstance().setAccount();
    }

    @FXML
    private void navigateToSettings() throws Exception {
        SceneHandlerAdmin.getInstance().setSettings();
    }
}