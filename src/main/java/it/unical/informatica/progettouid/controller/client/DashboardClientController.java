package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.Corsi;
import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.model.PersonalTrainer;
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

import java.util.List;


public class DashboardClientController {
    @FXML
    public ListView<HBox> corsiListView;
    @FXML
    private BorderPane mainPane;

    public void initialize() {
        loadCorsiOggi();
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
