package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.*;
import it.unical.informatica.progettouid.view.AlertManager;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Optional;


public class DashboardClientController {
    @FXML public ListView<HBox> corsiListView;
    public Label bentornatoLabel;
    @FXML public VBox vboxCenter;
    @FXML public ListView<HBox> notificationListView;
    @FXML public VBox vboxAccessi;


    public void initialize() {
        bentornatoLabel.setText(STR."Bentornato \{ClientSession.getInstance().getCurrentClient().nome()}");
        mostraStatoAbbonamento();
        loadCorsiOggi();
        loadNotifiche();
    }

    private void mostraStatoAbbonamento() {
        Task<InfoAccessiAbbonamento> task = DBConnection.getInstance().getAccessiRimanenti();

        Thread thread = new Thread(task);
        thread.start();

        task.setOnSucceeded(event -> {
            InfoAccessiAbbonamento abbonamento= task.getValue();
            if(abbonamento == null){
                Label nessunAbbonamento = new Label("Non è abbonato, scopri i piani disponibili e adatti a te: ");
                Button piani = new Button("Scopri");
                piani.setOnAction(e-> {
                    try {
                        SceneHandlerClient.getInstance().setAbbonamentoView();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
                vboxAccessi.getChildren().addAll(nessunAbbonamento, piani);

            }
            else{
                VBox info = new VBox(10);
                HBox accessi = new HBox(10);
                Label accessiRimanentiText = new Label(STR."Accessi rimanenti: \{abbonamento.accessiRimanenti()}");
                Label accessiTotaliText = new Label(STR."Accessi totali: \{abbonamento.accessiTotali()}");
                accessi.getChildren().addAll(accessiRimanentiText, accessiTotaliText);

                Label scadenzaAbbText = new Label(STR."Scadenza abbonamento: \{abbonamento.dataScadenza()}");
                Label tipoAbb = new Label(STR."Tipo abbonamento: \{abbonamento.tipoAbbonamento()}");

                info.getChildren().addAll(accessi, scadenzaAbbText, tipoAbb);

                vboxAccessi.getChildren().add(info);
            }

        });

        task.setOnFailed(event -> {
            System.out.println(STR."Errore durante il caricamento dei dati del client: \{task.getException().getMessage()}");
        });

    }

    private void loadCorsiOggi() {
        Task<List<Corsi>> task = DBConnection.getInstance().getCorsiDiOggi();

        task.setOnSucceeded(event -> {
            List<Corsi> corsi = task.getValue();

            displayCorsi(corsi);

        });

        task.setOnFailed(event -> {
            System.out.println(STR."Errore durante il caricamento dei corsi di oggi (listVirw): \{task.getException().getMessage()}");
        });

        new Thread(task).start();
    }

    private void displayCorsi(List<Corsi> corsi) {
        corsiListView.getItems().clear();
        corsiListView.setFixedCellSize(90);
        corsiListView.setPrefHeight(corsi.size() * corsiListView.getFixedCellSize() +5);

        VBox.setVgrow(corsiListView, Priority.ALWAYS);

        if (corsi.isEmpty()) {
            HBox emptyContent = new HBox();
            emptyContent.setAlignment(Pos.CENTER);
            Label emptyMessage = new Label("Nessun corso disponibile per oggi");
            emptyContent.getChildren().add(emptyMessage);
            corsiListView.getItems().clear();
            corsiListView.getItems().add(emptyContent);
            return;
        }

        for(Corsi c: corsi){
            HBox content = new HBox(10);
            content.setAlignment(Pos.CENTER_LEFT);
            content.setPadding(new Insets(0, 16, 0, 16));

            Label nomeCorso = new Label(c.nome());

            Label nomeTrainer = new Label(c.nomeTrainer());
            Label cognomeTrainer = new Label(c.cognomeTrainer());

            Button prenota = new Button("Prenota");
            prenota.setOnAction(e -> {
                AlertManager conf = new AlertManager(Alert.AlertType.CONFIRMATION, "Conferma prenotazione", null, STR."Confermi la prenotazione per il corso \{c.nome()}?");
                Optional<ButtonType> result = conf.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    confermaPrenotazione(c.id());
                } else {
                    System.out.println("Cancellazione annullata.");
                }
            });

            content.getChildren().addAll(nomeCorso, nomeTrainer, cognomeTrainer, prenota);
            corsiListView.getItems().add(content);

        }
    }

    private void confermaPrenotazione(int idCorso) {
        Task<Boolean> checkTask = DBConnection.getInstance().hasPrenotazioneOggi(idCorso);
        Thread checkThread = new Thread(checkTask);
        checkThread.setDaemon(true);
        checkThread.start();

        checkTask.setOnSucceeded(e -> {
            if (checkTask.getValue()) {
                AlertManager alert = new AlertManager(Alert.AlertType.WARNING, "Errore", null,
                        "Hai già una prenotazione per questo corso oggi");
                alert.display();
                return;
            }
            salvaPrenotazioneCorso(idCorso);
        });
    }

    private void salvaPrenotazioneCorso(int idCorso) {
        Task<Boolean> task = DBConnection.getInstance().insertPrenotazioneDashboard(idCorso);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(e->{
            AlertManager al = new AlertManager(Alert.AlertType.CONFIRMATION, "Conferma", null, "Prenotazione confermata");
            al.display();
        });
        task.setOnFailed(e->{
            System.out.println(STR."Errore durante l'inserimento della prenotazione: \{task.getException()}");
        });
    }

    private void loadNotifiche() {
        Task<List<Notifica>> task = DBConnection.getInstance().getNotificheClient();

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
        notificationListView.getItems().clear();
        notificationListView.setFixedCellSize(90);
        notificationListView.setPrefHeight(notifiche.size() * notificationListView.getFixedCellSize() +5);
        //VBox.setVgrow(listArticoli, Priority.ALWAYS);

        // Permette alla ListView di adattarsi dinamicamente
        VBox.setVgrow(notificationListView, Priority.ALWAYS);

        if (notifiche == null || notifiche.isEmpty()) {
            HBox emptyContent = new HBox();
            emptyContent.setAlignment(Pos.CENTER);
            Label emptyMessage = new Label("Nessun notifiche");
            emptyContent.getChildren().add(emptyMessage);
            notificationListView.getItems().add(emptyContent);
        }
        for (Notifica notifica : notifiche) {
            HBox content = new HBox(10);
            content.setAlignment(Pos.CENTER_LEFT);
            Label message = new Label(notifica.message());

            content.getChildren().addAll(message);
            notificationListView.getItems().add(content);
        }
    }

    @FXML
    public void onNavigationButtonClick(ActionEvent event) {
        Button button = (Button) event.getSource();
        try {
            switch (button.getId()) {
                case "dashboardClient":
                    SceneHandlerClient.getInstance().setDashboardView();
                    break;
                case "attivitaClient":
                    SceneHandlerClient.getInstance().setAttivitaView();
                    break;
                case "prenotazionePT":
                    SceneHandlerClient.getInstance().setPrenotazioniView();
                    break;
                case "schedaClient":
                    SceneHandlerClient.getInstance().setSchedaView();
                    break;
                case "abbonamentoClient":
                    SceneHandlerClient.getInstance().setAbbonamentoView();
                    break;
                case "impostazioniClient":
                    SceneHandlerClient.getInstance().setImpostazioniView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
