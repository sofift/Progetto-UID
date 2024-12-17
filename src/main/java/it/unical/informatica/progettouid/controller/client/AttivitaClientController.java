package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.Corsi;
import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.model.OrariCorsi;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;


// DA MODIFICARE, SIA IL CSS E SIA LA GESTIONE DEL CALENDARIO

public class AttivitaClientController {
    @FXML private FlowPane corsiFlowPane;
    @FXML private VBox orariCorsiVBox;

    @FXML
    public void initialize() {
        loadCorsi();
    }

    private void loadCorsi() {
        Task<List<Corsi>> taskCorsi = DBConnection.getInstance().getCorsi();

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
            corso.setOnAction(event -> mostraOrari(c.id(), c.descrizione()));
            corsiFlowPane.getChildren().add(corso);
        }
    }

    private void mostraOrari(int idCorso, String descrizione) {
        Task<List<OrariCorsi>> taskOrari = DBConnection.getInstance().getOrarioCorsi(idCorso);
        taskOrari.setOnSucceeded(e->{
            List<OrariCorsi> orari = taskOrari.getValue();

            for(OrariCorsi c : orari){
                Label giorno = new Label(c.giorno());
                Label oraInizio = new Label(STR."Dalle \{c.oraInizio()}alle \{c.oraFine()}");
                Text testoDesc = new Text(descrizione);
                Button prenota = new Button("Prenota");
                prenota.setOnAction(event -> {
                    Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmDialog.setTitle("Conferma prenotazione");
                    confirmDialog.setHeaderText(null);
                    confirmDialog.setContentText("Confermi la prenotazione per il corso " + c.nomeCorso() + "?");

                    alert(confirmDialog);
                });
                orariCorsiVBox.getChildren().addAll(giorno, oraInizio, testoDesc, prenota);
            }

        });
    }

    static void alert(Alert confirmDialog) {
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Alert successDialog = new Alert(Alert.AlertType.INFORMATION);
            successDialog.setTitle("Successo");
            successDialog.setHeaderText(null);
            successDialog.setContentText("Prenotazione effettuata con successo!");
            successDialog.showAndWait();
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