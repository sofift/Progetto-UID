package it.unical.informatica.progettouid.controller.Trainer;

import it.unical.informatica.progettouid.model.Client;
import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.model.PersonalTrainer;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class CreaSchedaController {
    public Button aggiungiEsercizioButton;
    @FXML private ComboBox<Client> clienteComboBox;
    @FXML private DatePicker dataInizioPicker;
    @FXML private TextField durataField;
    //@FXML private TableView<Esercizio> eserciziTable;
    @FXML private VBox schedaInfoPanel;
    @FXML private VBox aggiungiEsercizioPanel;
    @FXML private ComboBox<String> esercizioComboBox;
    @FXML private Spinner<Integer> serieSpinner;
    @FXML private Spinner<Integer> ripetizioniSpinner;
    @FXML private Spinner<Integer> recuperoSpinner;
    @FXML private TextArea noteEsercizioTextArea;

    @FXML
    private void initialize() {
        // Inizializzazione dei componenti
        mostraClienti();
    }

    private void mostraClienti() {
        Task<List<Client>> task = DBConnection.getInstance().getAllClient();

        task.setOnSucceeded(event -> {
            clienteComboBox.getItems().clear();
            List<Client> clients = task.getValue();
            if (clients.isEmpty()) {
                showAlertSucc("Errore", "Erorre nel caricamento dei client");
            } else {
                for (Client client : clients) {
                    clienteComboBox.getItems().add(client);
                }
            }
        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento dei personal(listVirw): " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    public void formAggiungiEsercizio(ActionEvent actionEvent) {
        schedaInfoPanel.setVisible(false);
        aggiungiEsercizioPanel.setVisible(true);
    }


    @FXML
    private void aggiungiEsercizio() {
        String nomeEsercizio = esercizioComboBox.getValue();
        if (nomeEsercizio == null || nomeEsercizio.trim().isEmpty()) {
            mostraErrore("Devi selezionare o inserire un esercizio!");
            return;
        }

        /*Esercizio nuovoEsercizio = new Esercizio(

                nomeEsercizio,
                serieSpinner.getValue(),
                ripetizioniSpinner.getValue(),
                recuperoSpinner.getValue() + " sec",
                noteEsercizioTextArea.getText()
        );

        eserciziTable.getItems().add(nuovoEsercizio);
        pulisciFormEsercizio();
        mostraPannelloPrincipale();*/
    }

    @FXML
    private void salvaScheda() {
        // Salvare la scheda nel database
    }

    @FXML
    private void anteprimaPDF() {
        // Generare anteprima PDF della scheda
    }

    private void showAlertSucc(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void annullaAggiuntaEsercizio(ActionEvent actionEvent) {
        pulisciFormEsercizio();
        mostraPannelloPrincipale();
    }

    private void pulisciFormEsercizio() {
        esercizioComboBox.setValue(null);
        serieSpinner.getValueFactory().setValue(3);
        ripetizioniSpinner.getValueFactory().setValue(12);
        recuperoSpinner.getValueFactory().setValue(90);
        noteEsercizioTextArea.clear();
    }

    private void mostraPannelloPrincipale() {
        schedaInfoPanel.setVisible(true);
        aggiungiEsercizioPanel.setVisible(false);
    }

    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }


}




