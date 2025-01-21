package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.Corsi;
import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.model.OrariCorsi;
import it.unical.informatica.progettouid.view.AlertManager;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;

// TODO button prenota non funziona
// aggiornare tabella prenotaizoni

public class AttivitaClientController {
    @FXML private FlowPane corsiFlowPane;
    @FXML private VBox vboxCenter;

    @FXML
    public void initialize() {
        loadCorsi();
    }

    private void loadCorsi() {
        Task<List<Corsi>> taskCorsi = DBConnection.getInstance().getCorsi();
        Thread thread = new Thread(taskCorsi);
        thread.setDaemon(true);
        thread.start();

        taskCorsi.setOnSucceeded(e->{
            List<Corsi> corsi = taskCorsi.getValue();
            displayButtonCorsi(corsi);
        });

        taskCorsi.setOnFailed(e->{
            System.out.println(STR."Errore durante il caricamento dei corsi : \{taskCorsi.getException().getMessage()}");
        });
    }

    public void displayButtonCorsi(List<Corsi> corsi) {
        corsiFlowPane.getChildren().clear();
        for(Corsi c: corsi){
            Button corso = new Button(c.nome());
            // Aggiungi classe CSS per lo stile del bottone corso
            corso.getStyleClass().add("course-button");
            corso.setOnAction(event -> mostraOrari(c.id(), c.descrizione()));

            // Crea un VBox per contenere il corso (opzionale per layout migliore)
            VBox courseCard = new VBox(10); // spacing 10
            courseCard.getStyleClass().add("course-card");
            courseCard.getChildren().add(corso);

            corsiFlowPane.getChildren().add(courseCard);
        }
    }

    private void mostraOrari(int idCorso, String descrizione) {
        Task<List<OrariCorsi>> taskOrari = DBConnection.getInstance().getOrarioCorsi(idCorso);
        Thread thread = new Thread(taskOrari);
        thread.setDaemon(true);
        thread.start();
        taskOrari.setOnSucceeded(e->{
            List<OrariCorsi> orari = taskOrari.getValue();

            displayOrariCorsi(orari, descrizione);


        });
        taskOrari.setOnFailed(e->{
            System.out.println(STR."Errore durante il caricamento dei corsi \{taskOrari.getException()}");
        });
    }

    private void displayOrariCorsi(List<OrariCorsi> orari, String descrizione) {
        ListView<HBox> listviewCorsi = new ListView<>();
        listviewCorsi.setFixedCellSize(90);
        listviewCorsi.setPrefHeight(orari.size() * listviewCorsi.getFixedCellSize() + 5);
        VBox.setVgrow(listviewCorsi, Priority.ALWAYS);

        for(OrariCorsi c : orari){
            HBox content = new HBox(20);
            content.setAlignment(Pos.CENTER_LEFT);
            content.setPadding(new Insets(0, 16, 0, 16));
            VBox textContainer = new VBox(5);
            Label nomeCorso = new Label(c.nomeCorso());
            nomeCorso.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            Label giornoOra = new Label(STR."Tutti i \{c.giorno()}, dalle \{c.oraInizio()} alle \{c.oraFine()}");

            Label testoDesc = new Label(descrizione);
            testoDesc.setWrapText(true);

            textContainer.getChildren().addAll(nomeCorso, giornoOra, testoDesc);
            Button prenota = new Button("Prenota");

            prenota.setOnAction(event -> {
                confermaPrenotazione(c);
            });
            content.getChildren().addAll(textContainer, prenota);
            listviewCorsi.getItems().addAll(content);
        }
        vboxCenter.getChildren().add(listviewCorsi);
    }

    private void confermaPrenotazione(OrariCorsi corso) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Conferma prenotazione");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText(STR."Confermi la prenotazione per il corso \{corso.nomeCorso()}?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Void> task = DBConnection.getInstance().insertPrenotazioneCorso(corso.idCorso(), corso.giorno());

            task.setOnSucceeded(e->{
                AlertManager alert = new AlertManager(Alert.AlertType.CONFIRMATION, "Successo", null, "Prenotazione avvenuta con successo");
                alert.display();
            });

            task.setOnFailed(e->{
                task.getException().printStackTrace();
            });


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