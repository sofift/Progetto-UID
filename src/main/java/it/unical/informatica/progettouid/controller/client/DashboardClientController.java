package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.*;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;


public class DashboardClientController {
    @FXML public ListView<HBox> corsiListView;
    public Label bentornatoLabel;
    @FXML public VBox vboxCenter;
    @FXML public ListView<HBox> notificationListView;
    @FXML public VBox vboxAccessi;


    public void initialize() {
        bentornatoLabel.setText(STR."Bentornato \{ClientSession.getInstance().getCurrentClient().getNome()}");
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
                Label nessunAbbonamento = new Label("Non è abbonato, scopri i piani disponibili e adatti a lei: ");
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
                HBox accessi = new HBox(10);
                Label accessiRimanentiText = new Label(STR."\{abbonamento.accessiRimanenti()}");
                Label accessiTotaliText = new Label(STR."\{abbonamento.accessiTotali()}");
                VBox info = new VBox(10);
                Label scadenzaAbbText = new Label(abbonamento.dataScadenza());
                Label tipoAbb = new Label(abbonamento.tipoAbbonamento());

                info.getChildren().addAll(scadenzaAbbText, tipoAbb);
                accessi.getChildren().addAll(accessiRimanentiText, accessiTotaliText, info);
                vboxCenter.getChildren().add(accessi);
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
            System.out.println("Errore durante il caricamento dei corsi di oggi (listVirw): " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    private void displayCorsi(List<Corsi> corsi) {
        corsiListView.getItems().clear();
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
            // visualizza nome corso, orario, durata e personal
            content.setAlignment(Pos.CENTER_LEFT);

            Label nomeCorso = new Label(c.nome());
            //nomeCorso.setPrefWidth(150); // Fissa larghezza per allineamento

            //Label orario = new Label(c.oraInizio());
            //orario.setPrefWidth(100);

            Label trainer = new Label(c.PT());
            //trainer.setPrefWidth(150);

            //Label posti = new Label(String.valueOf(c.maxPartecipanti()));

            Button prenota = new Button("Prenota");
            prenota.setOnAction(e -> {
                Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDialog.setTitle("Conferma prenotazione");
                confirmDialog.setHeaderText(null);
                confirmDialog.setContentText("Confermi la prenotazione per il corso " + c.nome() + "?");

            });

            content.getChildren().addAll(nomeCorso /*orario*/, trainer /*posti*/, prenota);
            corsiListView.getItems().add(content);

        }
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
