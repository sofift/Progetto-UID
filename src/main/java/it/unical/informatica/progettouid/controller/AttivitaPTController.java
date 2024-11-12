// AttivitaPTController.java
package it.unical.informatica.progettouid.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class AttivitaPTController {
    @FXML private Button todayButton;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label currentDateLabel;
    @FXML private GridPane monthView;
    @FXML private GridPane weekView;
    @FXML private VBox dayView;
    @FXML private ToggleGroup viewToggleGroup;
    @FXML private ToggleButton dayToggle;
    @FXML private ToggleButton weekToggle;
    @FXML private ToggleButton monthToggle;

    private LocalDate currentDate = LocalDate.now();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ITALIAN);
    private static final String[] DAYS_OF_WEEK = {"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};

    @FXML
    public void initialize() {
        setupCalendarControls();
        setupViewToggles();
        initializeMonthView();
        updateCalendarView();
    }

    private void setupCalendarControls() {
        todayButton.setOnAction(e -> {
            currentDate = LocalDate.now();
            updateCalendarView();
        });

        prevButton.setOnAction(e -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendarView();
        });

        nextButton.setOnAction(e -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendarView();
        });
    }

    private void setupViewToggles() {
        dayToggle.setOnAction(e -> switchView("day"));
        weekToggle.setOnAction(e -> switchView("week"));
        monthToggle.setOnAction(e -> switchView("month"));
    }

    private void initializeMonthView() {
        // Set up the month view grid
        monthView.getStyleClass().add("calendar-grid");
        monthView.setGridLinesVisible(true);

        // Add column constraints
        for (int i = 0; i < 7; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / 7);
            monthView.getColumnConstraints().add(column);
        }

        // Add row constraints
        for (int i = 0; i < 7; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            row.setMinHeight(60); // Set minimum height for rows
            monthView.getRowConstraints().add(row);
        }

        // Add day of week headers
        for (int i = 0; i < DAYS_OF_WEEK.length; i++) {
            Label dayLabel = new Label(DAYS_OF_WEEK[i]);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setAlignment(Pos.CENTER);
            monthView.add(dayLabel, i, 0);
        }
    }

    private void updateCalendarView() {
        currentDateLabel.setText(currentDate.format(dateFormatter));

        if (monthView.isVisible()) {
            updateMonthView();
        } else if (weekView.isVisible()) {
            updateWeekView();
        } else {
            updateDayView();
        }
    }

    private void updateMonthView() {
        // Clear existing day cells, keeping the headers
        monthView.getChildren().removeIf(node ->
                GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);

        YearMonth yearMonth = YearMonth.from(currentDate);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);

        // Get the day of week for the first day (1 = Monday, 7 = Sunday)
        int dayOfWeekValue = firstDayOfMonth.getDayOfWeek().getValue();

        // Calculate the first date to display (might be from previous month)
        LocalDate firstDateInCalendar = firstDayOfMonth.minusDays(dayOfWeekValue - 1);

        // Populate calendar grid
        for (int i = 1; i < 7; i++) {  // rows
            for (int j = 0; j < 7; j++) { // columns
                LocalDate date = firstDateInCalendar.plusDays((i - 1) * 7 + j);
                VBox dayCell = createDayCell(date, yearMonth);
                monthView.add(dayCell, j, i);
            }
        }
    }

    private VBox createDayCell(LocalDate date, YearMonth currentYearMonth) {
        VBox dayCell = new VBox(5);
        dayCell.setAlignment(Pos.TOP_CENTER);
        dayCell.getStyleClass().add("calendar-cell");

        // Add date number
        Label dateLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dateLabel.setMaxWidth(Double.MAX_VALUE);
        dateLabel.setAlignment(Pos.CENTER);

        // Style for dates not in current month
        if (!YearMonth.from(date).equals(currentYearMonth)) {
            dayCell.getStyleClass().add("other-month");
            dateLabel.setStyle("-fx-text-fill: #808080;");
        }

        // Style for today's date
        if (date.equals(LocalDate.now())) {
            dayCell.getStyleClass().add("today");
            dateLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ff0000;");
        }

        dayCell.getChildren().add(dateLabel);

        // Make the cell clickable
        dayCell.setOnMouseClicked(e -> handleDateClick(date));

        return dayCell;
    }

    private void handleDateClick(LocalDate date) {
        // Implement date click handling
        System.out.println("Clicked on date: " + date);
        // You can add logic here to show activities for the selected date
    }

    private void switchView(String view) {
        monthView.setVisible(view.equals("month"));
        weekView.setVisible(view.equals("week"));
        dayView.setVisible(view.equals("day"));
        updateCalendarView();
    }

    private void updateWeekView() {
        // To be implemented
    }

    private void updateDayView() {
        // To be implemented
    }
}