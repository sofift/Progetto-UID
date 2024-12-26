package it.unical.informatica.progettouid.controller.Trainer;

import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.model.Notifica;
import it.unical.informatica.progettouid.model.PrenotazionePT;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.util.List;

public class DashboardPTController {
    @FXML private ListView<HBox> prenotazioniList;
    @FXML private ListView<HBox> notificationsList;

    public void initialize() {
        loadPrenotazioni();
        loadNotifiche();
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
            Label emptyMessage = new Label("Nessuna notifica");
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

    private void loadNotifiche() {
        Task<List<Notifica>> task = DBConnection.getInstance().getNotificheNonLette();

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(event -> {
            List<Notifica> notifiche = task.getValue();
            displayNotifiche(notifiche);
        });

        task.setOnFailed(event -> {

        });
    }

    private void displayNotifiche(List<Notifica> notifiche) {
        notificationsList.getItems().clear();
        if (notifiche.isEmpty()) {
            HBox emptyContent = new HBox();
            emptyContent.setAlignment(Pos.CENTER);
            Label emptyMessage = new Label("Nessun notifiche");
            emptyContent.getChildren().add(emptyMessage);
            notificationsList.getItems().clear();
            notificationsList.getItems().add(emptyContent);
        }
        for (Notifica notifica : notifiche) {
            HBox content = new HBox(10);
            content.setAlignment(Pos.CENTER_LEFT);
            Label message = new Label(notifica.message());
            Button accetta = new Button("Accetta");
            Button rifiuta = new Button("Rifiuta");

            accetta.setOnAction(event -> {modificaRichiesta(notifica.id(), "accetta");});
            rifiuta.setOnAction(event -> {modificaRichiesta(notifica.id(), "rifiuta");});


            content.getChildren().addAll(message, accetta, rifiuta);
            notificationsList.getItems().add(content);
        }
    }

    private void modificaRichiesta(int id, String stato) {
        Task<Void> task = DBConnection.getInstance().updateNotifyPT(id, stato);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        task.setOnSucceeded(event -> {

        });

    }

    public void deleteNotify(ActionEvent actionEvent) {
        notificationsList.getItems().clear();
        HBox emptyContent = new HBox();
        emptyContent.setAlignment(Pos.CENTER);
        Label emptyMessage = new Label("Nessuna notifica");
        emptyContent.getChildren().add(emptyMessage);
        notificationsList.getItems().add(emptyContent);
    }


    /*@FXML
    public void onNavigationButtonClick(ActionEvent event) {
        Button button = (Button) event.getSource();
        try {
            switch (button.getId()) {
                case "dashboardClient":
                    SceneHandlerPT.getInstance().setDashboardView();
                    break;
                case "attivitaClient":
                    SceneHandlerPT.getInstance().setAttivitaView();
                    break;
                case "prenotazionePT":
                    SceneHandlerPT.getInstance().setPrenotazioniView();
                    break;
                case "schedaClient":
                    SceneHandlerPT.getInstance().setSchedaView();
                    break;
                case "abbonamentoClient":
                    SceneHandlerPT.getInstance().setAbbonamentoView();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
