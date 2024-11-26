package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.*;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.util.List;


public class DashboardClientController {
    @FXML
    public ListView<HBox> corsiListView;
    public Label bentornatoLabel;
    @FXML
    private BorderPane mainPane;
    @FXML
    public Text accessiRimanentiText;
    @FXML
    public Text accessiTotaliText;
    @FXML
    public Text scadenzaAbbText;
    private Client currentClient = null;


    public void initialize() {
        //currentClient = SessionManager.getInstance().getLoggedClient();
        bentornatoLabel.setText("Bentornato" );//+ currentClient.getNome());
        mostraStatoAbbonamento();
        loadCorsiOggi();
    }

    private void mostraStatoAbbonamento() {
        Task<Abbonamento> task = DBConnection.getInstance().getAccessiRimanenti(currentClient.getId());

        task.setOnSucceeded(event -> {
            Abbonamento abbonamento= task.getValue();

            accessiRimanentiText.setText("" + abbonamento.getAccessiRimanenti());
            accessiTotaliText.setText("" + abbonamento.getAccessiTotali());
            scadenzaAbbText.setText(abbonamento.getDataScadenza());
        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento dei dati del client: " + task.getException().getMessage());
        });

        new Thread(task).start();

    }

    private void loadCorsiOggi() {
        Task<List<Corsi>> task = DBConnection.getInstance().getCorsiDiOggi();

        task.setOnSucceeded(event -> {
            List<Corsi> corsi = task.getValue();
            for (Corsi c : corsi) {
                System.out.println(c);
            }
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

            Label nomeCorso = new Label(c.getNome());
            nomeCorso.setPrefWidth(150); // Fissa larghezza per allineamento

            Label orario = new Label(c.getOraInizio());
            orario.setPrefWidth(100);

            Label trainer = new Label(c.getPT());
            trainer.setPrefWidth(150);

            Label posti = new Label(String.valueOf(c.getMaxPartecipanti()));

            Button prenota = new Button("Prenota");

            content.getChildren().addAll(nomeCorso, orario, trainer, posti, prenota);
            corsiListView.getItems().add(content);

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
