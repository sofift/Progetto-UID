package it.unical.informatica.progettouid.controller;

import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class RegistrazioneController {
    @FXML private TextField cognomeField;
    @FXML private DatePicker dataNascitaPicker;
    @FXML private TextField emailField;
    @FXML private Label messageLabel;
    @FXML private TextField nomeField;
    @FXML private PasswordField passwordField;
    @FXML private VBox regVbox;
    @FXML private Label successoRegLabel;
    @FXML private VBox successoVbox;

    @FXML
    void accediButton(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        SceneHandlerPrimaPagina.getInstance().loadPrimaPagina();
    }


    @FXML
    void annullaRegistrazione(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        SceneHandlerPrimaPagina.getInstance().loadPrimaPagina();
    }

    @FXML
    void registraClient(ActionEvent event) {
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        LocalDate dataNascita = dataNascitaPicker.getValue();
        String email = emailField.getText();
        String password = passwordField.getText();
        if (!nome.isEmpty() || !cognome.isEmpty()  || !email.isEmpty() || !password.isEmpty() || emailValida(email)) {
            try {
                String dataNascitaString = dataNascita.toString();

                Task<Boolean> task = DBConnection.getInstance().insertClient(nome, cognome, dataNascitaString, email, password);

                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();

                System.out.println(task.getValue());
                task.setOnSucceeded(e->{
                    showSuccessDialog();
                });



            } catch (Exception e) {
                messageLabel.setText("Errore durante la registrazione: " + e.getMessage());
            }
        }
    }

    private void showSuccessDialog() {
        regVbox.setVisible(false);
        successoVbox.setVisible(true);
        successoRegLabel.setText("Registrazione avvenuta con successo! \n Accedi subito per usufruire dei nostri servizi" );
    }

    private boolean emailValida(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return Pattern.matches(emailRegex, email);
    }


}
