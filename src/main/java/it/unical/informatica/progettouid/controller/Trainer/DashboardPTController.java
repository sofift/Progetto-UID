package it.unical.informatica.progettouid.controller.Trainer;

import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.model.Notifica;
import it.unical.informatica.progettouid.model.PTSession;
import it.unical.informatica.progettouid.model.PrenotazionePT;
import it.unical.informatica.progettouid.view.AlertManager;
import it.unical.informatica.progettouid.view.SceneHandlerPT;
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
    @FXML private Label benvenutoLabel;

    public void initialize() {
        prenotazioniList.setFixedCellSize(60);
        notificationsList.setFixedCellSize(60);// Altezza fissa per ogni cella (in pixel)
        String nomePT = PTSession.getInstance().getCurrentTrainer().nome();
        benvenutoLabel.setText(STR."Benvenuto \{nomePT}");
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
            Label emptyMessage = new Label("Nessuna prenotazione");
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
            prenotazioniList.setPrefHeight(prenotazioni.size() * prenotazioniList.getFixedCellSize());
        }
    }

    private void loadNotifiche() {
        Task<List<Notifica>> task = DBConnection.getInstance().getNotificheNonLette();
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(event -> {
            List<Notifica> notifiche = task.getValue();
            System.out.println(STR."Notifiche caricate: \{notifiche.size()}"); // Debug
            if (notifiche.isEmpty()) {
                System.out.println("Nessuna notifica da visualizzare."); // Debug
            }
            displayNotifiche(notifiche);
        });

        task.setOnFailed(event -> {
            System.out.println(STR."Errore durante il caricamento delle notifiche\{task.getException()}");
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

            accetta.setOnAction(event -> {
                modificaRichiesta(notifica.id(), "accettata");
                accetta.setDisable(true);
                rifiuta.setDisable(true);
            });

            rifiuta.setOnAction(event -> {
                modificaRichiesta(notifica.id(), "rifiutata");
                rifiuta.setDisable(true);
                accetta.setDisable(true);
            });


            content.getChildren().addAll(message, accetta, rifiuta);
            notificationsList.getItems().add(content);
            notificationsList.setPrefHeight(notifiche.size() * notificationsList.getFixedCellSize());

        }
    }

    private void modificaRichiesta(int id, String stato) {
        Task<Void> task = DBConnection.getInstance().updateNotifyPT(id, stato);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        task.setOnSucceeded(event -> {
            if(stato == "accetta"){
                AlertManager al = new AlertManager(Alert.AlertType.CONFIRMATION, "Accettata", null, "Richiesta accettata");
                al.display();
            }else if(stato == "rifiuta"){
                AlertManager al = new AlertManager(Alert.AlertType.CONFIRMATION, "Rifiutata", null, "Richiesta rifiutata");
                al.display();
            }

        });

        task.setOnFailed(event->{
            System.out.println(STR."Errore durante la modifica della richiesta: \{task.getException()}");
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


    @FXML
    public void onNavigationButtonClick(ActionEvent event) {
        Button button = (Button) event.getSource();
        try {
            switch (button.getId()) {
                case "dashboardTrainer":
                    SceneHandlerPT.getInstance().setDashboardView();
                    break;
                case "attivitaPT":
                    SceneHandlerPT.getInstance().setAttivitaPTView();
                    break;
                case "creazioneScheda":
                    SceneHandlerPT.getInstance().setCreazioneSchedaView();
                    break;
                case "impostazioniPT":
                    SceneHandlerPT.getInstance().setImpostazioniView();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
