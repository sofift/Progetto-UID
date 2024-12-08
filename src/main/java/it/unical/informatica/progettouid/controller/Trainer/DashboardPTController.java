package it.unical.informatica.progettouid.controller.Trainer;

import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.model.PrenotazionePT;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import javax.management.Notification;
import java.util.List;

public class DashboardPTController {
    @FXML private ListView<HBox> prenotazioniList;
    @FXML private ListView<Notification> notificationsList;

    public void initialize() {
        loadPrenotazioni();
    }

    private void loadPrenotazioni() {
        prenotazioniList.getItems().clear();

        Task<List<PrenotazionePT>> task = DBConnection.getInstance().getPrenotazioneTrainer();

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(event -> {
            List<PrenotazionePT> prenotazioni = task.getValue();

            displayPrenotazioni(prenotazioni);

        });
        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento dei corsi di oggi (listVirw): " + task.getException().getMessage());
        });

        new Thread(task).start();

    }

    private void displayPrenotazioni(List<PrenotazionePT> prenotazioni) {
        prenotazioniList.getItems().clear();
        if (prenotazioni.isEmpty()) {
            HBox emptyContent = new HBox();
            emptyContent.setAlignment(Pos.CENTER);
            Label emptyMessage = new Label("Nessun prenotazine disponibile per oggi");
            emptyContent.getChildren().add(emptyMessage);
            prenotazioniList.getItems().clear();
            prenotazioniList.getItems().add(emptyContent);
            return;
        }
        for (PrenotazionePT p : prenotazioni) {
            HBox content = new HBox(10);
            // visualizza nome corso, orario, durata e personal
            content.setAlignment(Pos.CENTER_LEFT);

            Label nomeClient = new Label(p.nomeClient());
            //nomeCorso.setPrefWidth(150); // Fissa larghezza per allineamento

            Label cognomeClient = new Label(p.cognomeClient());
            //orario.setPrefWidth(100);

            Label data = new Label(p.data());
            //trainer.setPrefWidth(150);

            Label orario = new Label(p.oraPrenotazione());
            Label notes = new Label(p.notes());

            content.getChildren().addAll(nomeClient, cognomeClient, data, orario, notes);
            prenotazioniList.getItems().add(content);

        }
    }

}
