package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.Corsi;
import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.model.InfoBaseAbbonamento;
import it.unical.informatica.progettouid.model.TipiAbbonamento;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class AbbonamentoClientController {

    @FXML public Label tipoAbbonamentoLabel;
    @FXML public Label dataInizioLabel;
    @FXML public Label dataScadenzaLabel;
    @FXML public Label statoAbbonamentoLabel;
    @FXML public VBox vBoxPianiOfferti;
    @FXML public Label descrizioneLabel;


    @FXML
    public void initialize() {
        loadInformazioniCliente();
        loadPianiOfferti();
    }

    private void loadInformazioniCliente() {
        Task<InfoBaseAbbonamento> task = DBConnection.getInstance().getInfoAbbonamento();

        task.setOnSucceeded(event -> {
            InfoBaseAbbonamento info = task.getValue();

            displayInfoAbbonamento(info);

        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento delle informazioni: " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    private void displayInfoAbbonamento(InfoBaseAbbonamento info) {
        tipoAbbonamentoLabel.setText(info.nomeAbbonamento());
        dataInizioLabel.setText(info.dataInizio());
        dataScadenzaLabel.setText(info.dataScadenza());
        statoAbbonamentoLabel.setText(info.stato());
    }

    private void loadPianiOfferti() {
        Task<List<TipiAbbonamento>> task = DBConnection.getInstance().getAllPianiAbbonamento();

        task.setOnSucceeded(event -> {
            List<TipiAbbonamento> piani = task.getValue();
            for (TipiAbbonamento p : piani) {
                System.out.println(p);
            }
            displayPiani(piani);

        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento dei piani(listVirw): " + task.getException().getMessage());
        });

        new Thread(task).start();

    }

    private void displayPiani(List<TipiAbbonamento> piani) {
        vBoxPianiOfferti.getChildren().clear();

        for(TipiAbbonamento p : piani) {
            VBox vbox = new VBox();
            Label nome = new Label(p.nome());
            Label destinatoA = new Label(p.dedicatoA());
            Label prezzo = new Label("" + p.prezzo());

            Text descrizione = new Text(p.descrizione());
            Button seleziona = new Button("Seleziona");

            vbox.getChildren().addAll(nome, destinatoA, prezzo, descrizione, seleziona);
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
