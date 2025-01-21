package it.unical.informatica.progettouid.controller.Admin;

import it.unical.informatica.progettouid.view.SceneHandlerAdmin;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    @FXML private BorderPane root;
    @FXML private BarChart<String, Number> attendanceChart;
    @FXML private ListView<String> bookingsList;
    @FXML private ListView<String> notificationsList;
    @FXML private HBox statsContainer;
    @FXML private VBox mainContent;

    private String currentUser;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing AdminDashboardController");
        System.out.println("statsContainer: " + (statsContainer != null ? "found" : "null"));
        System.out.println("root: " + (root != null ? "found" : "null"));

        if (root == null || statsContainer == null || mainContent == null) {
            System.err.println("Critical error: Some FXML components were not properly initialized");
            return;
        }

        setupResponsiveness();
        initializeAttendanceChart();
        initializeListViews();
        setupListViewListeners();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setupResponsiveness() {
        if (root != null && statsContainer != null && mainContent != null) {
            root.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.widthProperty().addListener((observable, oldValue, newValue) -> {
                        double width = newValue.doubleValue();
                        if (width < 800) {
                            statsContainer.setSpacing(10);
                            mainContent.setSpacing(10);
                            statsContainer.setPrefWidth(width - 40);
                        } else {
                            statsContainer.setSpacing(20);
                            mainContent.setSpacing(20);
                            statsContainer.setPrefWidth(width - 60);
                        }
                    });
                }
            });

            if (mainContent != null) {
                mainContent.heightProperty().addListener((obs, oldVal, newVal) -> {
                    if (attendanceChart != null && statsContainer != null) {
                        double height = newVal.doubleValue();
                        double listViewHeight = (height - attendanceChart.getHeight() - statsContainer.getHeight() - 100) / 2;
                        if (bookingsList != null) bookingsList.setPrefHeight(listViewHeight);
                        if (notificationsList != null) notificationsList.setPrefHeight(listViewHeight);
                    }
                });
            }
        }
    }

    private void initializeAttendanceChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Presenze Giornaliere");

        series.getData().add(new XYChart.Data<>("Lun", 45));
        series.getData().add(new XYChart.Data<>("Mar", 38));
        series.getData().add(new XYChart.Data<>("Mer", 52));
        series.getData().add(new XYChart.Data<>("Gio", 41));
        series.getData().add(new XYChart.Data<>("Ven", 44));
        series.getData().add(new XYChart.Data<>("Sab", 35));
        series.getData().add(new XYChart.Data<>("Dom", 25));

        attendanceChart.getData().add(series);
        attendanceChart.setAnimated(false);
        attendanceChart.setLegendVisible(false);

        attendanceChart.prefWidthProperty().bind(mainContent.widthProperty().subtract(40));
        attendanceChart.prefHeightProperty().bind(mainContent.heightProperty().multiply(0.4));
    }

    private void setupListViewListeners() {
        bookingsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedBooking = bookingsList.getSelectionModel().getSelectedItem();
                if (selectedBooking != null) {
                    showBookingDetails(selectedBooking);
                }
            }
        });

        notificationsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedNotification = notificationsList.getSelectionModel().getSelectedItem();
                if (selectedNotification != null) {
                    markNotificationAsRead(selectedNotification);
                }
            }
        });
    }

    private void showBookingDetails(String booking) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dettagli Prenotazione");
        alert.setHeaderText(booking);
        alert.setContentText("Qui puoi aggiungere ulteriori dettagli della prenotazione.");
        alert.showAndWait();
    }

    private void markNotificationAsRead(String notification) {
        notificationsList.getItems().remove(notification);
        // Qui puoi aggiungere la logica per marcare la notifica come letta nel database
    }

    private void initializeListViews() {
        bookingsList.getItems().addAll(
                "Mario Rossi - Corso di Yoga (10:00)",
                "Laura Bianchi - Sala Pesi (11:30)",
                "Giuseppe Verdi - Box (14:00)",
                "Anna Neri - Pilates (16:00)"
        );

        notificationsList.getItems().addAll(
                "Nuovo iscritto: Carlo Magni",
                "Pagamento ricevuto da Laura Bianchi",
                "Richiesta di cancellazione corso",
                "Manutenzione programmata - Sala 2"
        );

        bookingsList.setStyle("-fx-background-radius: 5; -fx-background-color: white;");
        notificationsList.setStyle("-fx-background-radius: 5; -fx-background-color: white;");
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void refreshDashboard() {
        // Aggiorna i dati del grafico
        attendanceChart.getData().clear();
        initializeAttendanceChart();

        // Aggiorna le liste
        bookingsList.getItems().clear();
        notificationsList.getItems().clear();
        initializeListViews();
    }

    public void setUsername(String username) {
        this.currentUser = username;
    }

    @FXML
    private void navigateToHome() throws Exception {
        SceneHandlerAdmin.getInstance().setDashboardView();
    }

    @FXML
    private void navigateToCheckIn() throws Exception {
        SceneHandlerAdmin.getInstance().setCheckIn();
    }

    @FXML
    private void navigateToMembers() throws Exception {
        SceneHandlerAdmin.getInstance().setClient();
    }

    @FXML
    private void navigateToAddUser() throws Exception {
        SceneHandlerAdmin.getInstance().setAddUser();
    }

    @FXML
    private void navigateToAddPT() throws Exception {
        SceneHandlerAdmin.getInstance().setAddPT();
    }

    @FXML
    private void navigateToAddCourse() throws Exception {
        SceneHandlerAdmin.getInstance().setAddCourse();
    }

    @FXML
    private void navigateToBilling() throws Exception {
        SceneHandlerAdmin.getInstance().setBilling();
    }

    @FXML
    private void navigateToAccount() throws Exception {
        SceneHandlerAdmin.getInstance().setAccount();
    }

    @FXML
    private void navigateToSettings() throws Exception {
        SceneHandlerAdmin.getInstance().setSettings();
    }
}
