package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.*;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SchedaClientController implements Initializable {
    @FXML public TextArea sugAlimentari;
    @FXML private GridPane infoGrid;
    @FXML private TabPane weekdayTabs;
    @FXML private TextArea notesArea;
    @FXML private VBox ptInfoContainer;
    @FXML private VBox statsContainer;

    // VBox per ogni giorno della settimana
    @FXML private VBox lunedì;
    @FXML private VBox martedì;
    @FXML private VBox mercoledì;
    @FXML private VBox giovedì;
    @FXML private VBox venerdì;
    @FXML private VBox sabato;
    @FXML private VBox domenica;

    private int idScheda;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadSchedaInfo();

        // gestire id PT
        loadPTInfo();
        weekdayTabs.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                String giornoSelezionato = newTab.getText();
                System.out.println("Tab selezionata: " + giornoSelezionato);


                loadExercises(giornoSelezionato);
            }
        });

        setupStyles();
    }

    private void loadSchedaInfo() {
        Task<SchedaAllenamento> task = DBConnection.getInstance().getInfoSchedaClient();

        task.setOnSucceeded(event -> {
            SchedaAllenamento info = task.getValue();
            if (info != null) {
                idScheda = info.idScheda();
                displaySchedaInfo(info);
            } else {
                displayRichiestaScheda();
            }

        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento delle informazioni: " + task.getException().getMessage());
        });

        new Thread(task).start();

    }

    private void displayRichiestaScheda() {

    }

    private void displaySchedaInfo(SchedaAllenamento info) {
        Label dataInizio = new Label(info.dataInizio());
        Label dataFine = new Label(info.dataFine());
        Label obiettivo = new Label(info.obiettivi());
        Label stato = new Label(info.statoScheda());


        infoGrid.add(dataInizio, 1, 0);
        infoGrid.add(dataFine, 1, 1);
        infoGrid.add(obiettivo, 1, 2);
        infoGrid.add(stato, 1, 3);
        notesArea.setText(info.notes());
        sugAlimentari.setText(info.suggerimentiAlimentari());
    }

    private void loadPTInfo() {
        Task<PersonalTrainer> task = DBConnection.getInstance().getInfoPT();

        task.setOnSucceeded(event -> {
            PersonalTrainer info = task.getValue();

            displayPTInfo(info);

        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento delle informazioni: " + task.getException().getMessage());
        });

        new Thread(task).start();


    }

    private void displayPTInfo(PersonalTrainer info) {
        VBox ptCard = new VBox(10);
        //ptCard.getStyleClass().add("pt-card");

        Label nameLabel = new Label(info.getNome());
        //nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label specializzazioneLabel = new Label(info.getSpecializzazione());
        Label emailLabel = new Label(info.getEmail());
        Label phoneLabel = new Label(info.getTelefono());

        Button contactButton = new Button("Contatta PT");
        contactButton.setMaxWidth(Double.MAX_VALUE);
        contactButton.setOnAction(e -> contactPT());

        ptCard.getChildren().addAll(nameLabel, specializzazioneLabel, emailLabel, phoneLabel, contactButton);
        ptInfoContainer.getChildren().add(ptCard);
    }


    private void loadExercises(String giornoSelezionato) {
        // TODO: dipende dalla tab in cui mi trovo, iniviare gli esercizi e il giorno della settunana
        Task<List<EsercizioScheda>> task = DBConnection.getInstance().getEserciziGiorno(idScheda, giornoSelezionato);

        task.setOnSucceeded(event -> {
            List<EsercizioScheda> info = task.getValue();

            displayEsercizi(info, giornoSelezionato);

        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento delle informazioni: " + task.getException().getMessage());
        });

        new Thread(task).start();

        // Esempio per Lunedì



        // Ripeti per altri giorni...
    }

    // TODO: fare la logica epr mostrare gli esrvizi giorno epr giorno
    private void displayEsercizi(List<EsercizioScheda> info, String giornoSelezionato) {
        VBox giornoContainer = switch (giornoSelezionato.toLowerCase()) {
            case "lunedì" -> lunedì;
            case "martedì" -> martedì;
            case "mercoledì" -> mercoledì;
            case "giovedì" -> giovedì;
            case "venerdì" -> venerdì;
            case "sabato" -> sabato;
            case "domenica" -> domenica;
            default -> throw new IllegalArgumentException("Giorno non valido: " + giornoSelezionato);
        };

        giornoContainer.getChildren().clear(); // Pulisci eventuali esercizi precedenti

        for (EsercizioScheda esercizio : info) {
            HBox exerciseRow = new HBox(15);
            exerciseRow.setAlignment(Pos.CENTER_LEFT);
            exerciseRow.getStyleClass().add("exercise-row");

            Label nomeLabel = new Label(esercizio.nomeEserc());
            nomeLabel.setStyle("-fx-font-weight: bold;");
            Label serieRipLabel = new Label(esercizio.nSerie() + "x" + esercizio.nRipetizioni());
            Label recuperoLabel = new Label(esercizio.tmpRecupero() + " sec recupero");
            Label noteLabel = new Label(esercizio.notes());

            exerciseRow.getChildren().addAll(nomeLabel, serieRipLabel, recuperoLabel, noteLabel);
            giornoContainer.getChildren().add(exerciseRow);
        }
    }



    private void setupStyles() {
        // Aggiunta stili CSS
        String css = """
            .section-header {
                -fx-font-size: 16px;
                -fx-font-weight: bold;
            }
            .exercise-row {
                -fx-padding: 10;
                -fx-background-color: #f8f9fa;
                -fx-background-radius: 5;
            }
            .pt-card {
                -fx-padding: 15;
                -fx-background-color: white;
                -fx-border-color: #dee2e6;
                -fx-border-radius: 5;
            }
            .action-button {
                -fx-background-color: #007bff;
                -fx-text-fill: white;
            }
            .action-button:hover {
                -fx-background-color: #0056b3;
            }
        """;

        weekdayTabs.getStylesheets().add(css);
    }

    private void contactPT() {
        // Implementa la logica per contattare il PT
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Contatta PT");
        alert.setHeaderText(null);
        alert.setContentText("Invio messaggio al Personal Trainer...");
        alert.showAndWait();
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