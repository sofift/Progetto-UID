package it.unical.informatica.progettouid.controller.Admin;


import it.unical.informatica.progettouid.model.DBConnection;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class CheckInController {

    @FXML private Button btnCheckIn;
    @FXML private Button btnClear;
    @FXML private Button btnEnter;
    @FXML private TextField codeInput;
    @FXML private Label statusLabel;

    @FXML
    private void onSubmit(){
        String code = codeInput.getText();

        if (code.isEmpty()){
            statusLabel.setText("Inserire un codice valido.");
            return;
        }
        /*Task<Boolean> task = DBConnection.getInstance().checkIn(code);

        task.setOnSucceeded(event -> {
            statusLabel.setText("Check-in registrato con successo.");
            codeInput.clear();
        });
        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            if (exception instanceof IllegalArgumentException){
                statusLabel.setText(exception.getMessage());
            } else {
                statusLabel.setText("Errore durante il check-in.");
                exception.printStackTrace();
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();*/
    }
    @FXML
    private void onClear(){
        codeInput.clear();
        statusLabel.setText("");
    }
    @FXML
    private void onEnter(){
        onSubmit();
    }

}




