package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;

import java.net.URL;
import java.util.ResourceBundle;

public class SchedaClientController implements Initializable {

    @FXML private GridPane infoGrid;
    @FXML private TabPane weekdayTabs;
    @FXML private TextArea notesArea;
    @FXML private VBox ptInfoContainer;
    @FXML private VBox statsContainer;

    // VBox per ogni giorno della settimana
    @FXML private VBox mondayExercises;
    @FXML private VBox tuesdayExercises;
    @FXML private VBox wednesdayExercises;
    @FXML private VBox thursdayExercises;
    @FXML private VBox fridayExercises;
    @FXML private VBox saturdayExercises;
    @FXML private VBox sundayExercises;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadSchedaInfo();
        loadPTInfo();
        loadExercises();
        loadStats();
        setupStyles();
    }

    private void loadSchedaInfo() {
        // Esempio di caricamento informazioni
        Label dataInizio = new Label("01/03/2024");
        Label dataFine = new Label("01/06/2024");
        Label obiettivo = new Label("Ipertrofia e Forza");
        Label livello = new Label("Intermedio");

        infoGrid.add(dataInizio, 1, 0);
        infoGrid.add(dataFine, 1, 1);
        infoGrid.add(obiettivo, 1, 2);
        infoGrid.add(livello, 1, 3);

        notesArea.setText("- Eseguire gli esercizi con la corretta forma\n" +
                "- Rispettare i tempi di recupero indicati\n" +
                "- Bere molta acqua durante l'allenamento\n" +
                "- Fare sempre un adeguato riscaldamento");
    }

    private void loadPTInfo() {
        VBox ptCard = new VBox(10);
        ptCard.getStyleClass().add("pt-card");

        Label nameLabel = new Label("Mario Rossi");
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label certLabel = new Label("Certificazione ISSA");
        Label emailLabel = new Label("mario.rossi@gym.com");
        Label phoneLabel = new Label("+39 123 456 7890");

        Button contactButton = new Button("Contatta PT");
        contactButton.setMaxWidth(Double.MAX_VALUE);
        contactButton.setOnAction(e -> contactPT());

        ptCard.getChildren().addAll(nameLabel, certLabel, emailLabel, phoneLabel, contactButton);
        ptInfoContainer.getChildren().add(ptCard);
    }

    private void loadExercises() {
        // Esempio per Lunedì
        VBox mondayWorkout = createDayWorkout(new String[][]{
                {"Squat", "4x10", "2 min recupero"},
                {"Panca Piana", "4x8", "2 min recupero"},
                {"Stacchi", "3x12", "2 min recupero"},
                {"Lat Machine", "3x12", "1.5 min recupero"}
        });
        mondayExercises.getChildren().add(mondayWorkout);

        // Ripeti per altri giorni...
    }

    private VBox createDayWorkout(String[][] exercises) {
        VBox workout = new VBox(10);

        for (String[] exercise : exercises) {
            HBox exerciseRow = new HBox(15);
            exerciseRow.setAlignment(Pos.CENTER_LEFT);
            exerciseRow.getStyleClass().add("exercise-row");

            Label nameLabel = new Label(exercise[0]);
            nameLabel.setStyle("-fx-font-weight: bold;");
            Label setsRepsLabel = new Label(exercise[1]);
            Label restLabel = new Label(exercise[2]);

            exerciseRow.getChildren().addAll(nameLabel, setsRepsLabel, restLabel);
            workout.getChildren().add(exerciseRow);
        }

        return workout;
    }

    private void loadStats() {
        // Esempio di statistiche
        VBox stats = new VBox(5);
        stats.getChildren().addAll(
                new Label("Settimana 4/12"),
                new Label("Completamento: 75%"),
                new Label("Esercizi totali: 24"),
                new Label("Peso totale: 2500kg")
        );
        statsContainer.getChildren().add(stats);
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