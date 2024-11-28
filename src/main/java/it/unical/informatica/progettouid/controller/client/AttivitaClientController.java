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
import javafx.scene.text.TextAlignment;

import java.time.*;
import java.time.format.*;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
        loadCorsiMensili();
    }

    private void loadCorsiMensili() {
        LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        Task<Map<LocalDate, List<Corsi>>> task = DBConnection.getInstance().getCorsiMensili(firstOfMonth, lastOfMonth);

        task.setOnSucceeded(event -> {
            corsiPerGiorno = task.getValue();
            //updateCalendar(); // Aggiorna il calendario con i nuovi corsi
        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento dei corsi: " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    @FXML
    private VBox calendarContainer;

    // Metodo per visualizzare il calendario mensile
    @FXML
    public void showMonthView() {
        calendarContainer.getChildren().clear(); // Pulizia della vista attuale
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.now();

        GridPane monthGrid = new GridPane();
        monthGrid.setHgap(10); // Spaziatura orizzontale
        monthGrid.setVgap(10); // Spaziatura verticale

        // Etichette per i giorni della settimana
        String[] daysOfWeek = {"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};
        for (int i = 0; i < daysOfWeek.length; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            dayLabel.setTextAlignment(TextAlignment.CENTER);
            GridPane.setColumnIndex(dayLabel, i);
            GridPane.setRowIndex(dayLabel, 0);
            monthGrid.add(dayLabel, i, 0);
        }

        // Calcolo dei giorni del mese
        int daysInMonth = currentMonth.lengthOfMonth();
        LocalDate firstDayOfMonth = currentMonth.atDay(1);
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // 1 = Lun, ..., 7 = Dom

        // Inserimento dei giorni nella griglia
        int day = 1;
        for (int row = 1; row <= 6; row++) { // Al massimo 6 righe
            for (int col = 0; col < 7; col++) {
                if (row == 1 && col < firstDayOfWeek - 1) {
                    // Celle vuote prima dell'inizio del mese
                    continue;
                }
                if (day > daysInMonth) {
                    // Fine del mese
                    break;
                }

                // Creazione del nodo per il giorno
                Label dayLabel = new Label(String.valueOf(day));
                dayLabel.setStyle("-fx-font-size: 12px; -fx-alignment: center;");
                dayLabel.setTextAlignment(TextAlignment.CENTER);

                // Aggiunta alla griglia
                monthGrid.add(dayLabel, col, row);

                day++;
            }
        }

        // Aggiunta della griglia al contenitore
        calendarContainer.getChildren().add(monthGrid);
    }

    // Metodo per visualizzare il calendario settimanale
    @FXML
    public void showWeekView() {
        calendarContainer.getChildren().clear(); // Pulizia della vista attuale

        GridPane weekGrid = new GridPane();
        weekGrid.setHgap(10);
        weekGrid.setVgap(10);

        String[] daysOfWeek = {"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);

        for (int i = 0; i < daysOfWeek.length; i++) {
            Label dayLabel = new Label(daysOfWeek[i] + " " + startOfWeek.plusDays(i).getDayOfMonth());
            dayLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            dayLabel.setTextAlignment(TextAlignment.CENTER);
            weekGrid.add(dayLabel, i, 0);
        }

        calendarContainer.getChildren().add(weekGrid);
    }

    // Metodo per visualizzare il calendario giornaliero
    @FXML
    public void showDayView() {
        calendarContainer.getChildren().clear(); // Pulizia della vista attuale

        LocalDate today = LocalDate.now();
        Label dayLabel = new Label("Oggi: " + today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()) +
                " " + today.getDayOfMonth() + " " + today.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        dayLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        dayLabel.setTextAlignment(TextAlignment.CENTER);

        calendarContainer.getChildren().add(dayLabel);
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