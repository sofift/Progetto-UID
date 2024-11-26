package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.Corsi;
import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.time.*;
import java.time.format.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// DA MODIFICARE, SIA IL CSS E SIA LA GESTIONE DEL CALENDARIO

public class AttivitaClientController {
    @FXML private GridPane calendarGrid;
    @FXML private GridPane miniCalendar;
    @FXML private Label currentDateLabel;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Button todayButton;

    private LocalDate currentDate = LocalDate.now();
    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
    private Map<LocalDate, List<Corsi>> corsiPerGiorno = new HashMap<>();
    @FXML
    public void initialize() {
        setupButtons();
        loadCorsiMensili();
        updateCalendar();
        updateMiniCalendar();
    }

    private void loadCorsiMensili() {
        LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        Task<Map<LocalDate, List<Corsi>>> task = DBConnection.getInstance().getCorsiMensili(firstOfMonth, lastOfMonth);

        task.setOnSucceeded(event -> {
            corsiPerGiorno = task.getValue();
            updateCalendar(); // Aggiorna il calendario con i nuovi corsi
        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento dei corsi: " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    private void setupButtons() {
        todayButton.setOnAction(e -> {
            currentDate = LocalDate.now();
            updateCalendar();
        });

        prevButton.setOnAction(e -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendar();
        });

        nextButton.setOnAction(e -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendar();
        });
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();
        currentDateLabel.setText(currentDate.format(monthFormatter));

        // Intestazioni giorni della settimana
        String[] days = {"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
            calendarGrid.add(dayLabel, i, 0);
        }

        // Calcola il primo giorno del mese
        LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() - 1;

        // Popola la griglia
        int daysInMonth = currentDate.lengthOfMonth();
        int row = 1;
        int col = dayOfWeek;

        for (int day = 1; day <= daysInMonth; day++) {
            VBox dayCell = createDayCell(day);
            calendarGrid.add(dayCell, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }

        // Imposta le dimensioni delle celle
        for (int i = 0; i < 7; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / 7);
            calendarGrid.getColumnConstraints().add(cc);
        }
    }

    private VBox createDayCell(int day) {
        VBox cell = new VBox(5);
        cell.setStyle("-fx-background-color: white; -fx-padding: 5; -fx-border-color: #e0e0e0;");
        cell.setMaxWidth(Double.MAX_VALUE);
        cell.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(cell, Priority.ALWAYS);

        Label dayLabel = new Label(String.valueOf(day));
        dayLabel.setStyle("-fx-font-weight: bold;");
        dayLabel.setMaxWidth(Double.MAX_VALUE);
        dayLabel.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox corsiContainer = new VBox(2);
        corsiContainer.setStyle("-fx-background-color: transparent;");

        scrollPane.setContent(corsiContainer);
        cell.getChildren().addAll(dayLabel, scrollPane);

        return cell;
    }

    private HBox createCorsoBox(Corsi corso) {
        HBox corsoBox = new HBox(5);
        corsoBox.setStyle("-fx-background-color: #e3f2fd; -fx-padding: 5; " +
                "-fx-background-radius: 5; -fx-border-radius: 5;");
        corsoBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(corsoBox, Priority.ALWAYS);

        Label timeLabel = new Label(corso.getOraInizio());
        Label nameLabel = new Label(corso.getNome());
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        Button prenotaBtn = new Button("👤");
        prenotaBtn.setStyle("-fx-font-size: 10; -fx-padding: 2;");

        corsoBox.getChildren().addAll(timeLabel, nameLabel, prenotaBtn);
        return corsoBox;
    }

    private void showNewActivityDialog(LocalDate date) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Nuova Attività");

        // Crea form per nuova attività
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomeField = new TextField();
        TextField descrizioneField = new TextField();
        ComboBox<String> tipoBox = new ComboBox<>();
        tipoBox.getItems().addAll("Corso", "Evento", "Manutenzione");

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nomeField, 1, 0);
        grid.add(new Label("Descrizione:"), 0, 1);
        grid.add(descrizioneField, 1, 1);
        grid.add(new Label("Tipo:"), 0, 2);
        grid.add(tipoBox, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait();
    }

    private void updateMiniCalendar() {
        // Implementazione simile a updateCalendar() ma più compatta
        // ...
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