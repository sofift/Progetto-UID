package it.unical.informatica.progettouid.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.time.*;
import java.time.format.*;

public class AttivitaClientController {
    @FXML private GridPane calendarGrid;
    @FXML private GridPane miniCalendar;
    @FXML private Label currentDateLabel;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Button todayButton;

    private LocalDate currentDate = LocalDate.now();
    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");

    @FXML
    public void initialize() {
        setupButtons();
        updateCalendar();
        updateMiniCalendar();
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
        cell.setStyle("-fx-padding: 5; -fx-border-color: #e0e0e0;");

        Label dayLabel = new Label(String.valueOf(day));
        dayLabel.setStyle("-fx-font-weight: bold;");

        cell.getChildren().add(dayLabel);

        // Aggiungi gestione click per aggiungere nuove attività
        cell.setOnMouseClicked(e -> showNewActivityDialog(LocalDate.of(
                currentDate.getYear(),
                currentDate.getMonth(),
                day
        )));

        return cell;
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
}