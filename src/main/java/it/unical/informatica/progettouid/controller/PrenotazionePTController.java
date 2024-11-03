package it.unical.informatica.progettouid.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PrenotazionePTController implements Initializable {    // l'interfaccia Initializable permette di usare il metodo initialize per caricare i dati alla creazione del controller.

    @FXML
    private VBox trainerListContainer;

    @FXML
    private VBox bookingFormContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTrainers();
    }

    // caricamento dei trainer (da collegare al DB)
    private void loadTrainers() {
        // Dati di esempio dei trainer
        String[][] trainers = {
                {"Mario Rossi", "Specializzato in bodybuilding", "5 anni di esperienza"},
                {"Laura Bianchi", "Esperta in fitness funzionale", "8 anni di esperienza"},
                {"Giovanni Verdi", "Personal trainer certification ISSA", "3 anni di esperienza"}
        };

        for (String[] trainer : trainers) {
            trainerListContainer.getChildren().add(createTrainerCard(trainer[0], trainer[1], trainer[2]));
        }
    }

    // creazione sezione per ogni trainer --> il CSS è da mettere nel foglio di stile
    private VBox createTrainerCard(String name, String specialization, String experience) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; " +
                "-fx-border-radius: 5; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        card.setPadding(new Insets(15));
        card.setSpacing(5);

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Text specLabel = new Text(specialization);
        Text expLabel = new Text(experience);

        Button bookButton = new Button("Prenota sessione");
        bookButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        bookButton.setOnAction(e -> showBookingForm(name));

        card.getChildren().addAll(nameLabel, specLabel, expLabel, bookButton);
        return card;
    }

    private void showBookingForm(String trainerName) {
        bookingFormContainer.getChildren().clear();         // pulisce la aprte destra del border pane ogni votla che si preme sul button prenotazione

        Label title = new Label("Prenota sessione con " + trainerName);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> timeComboBox = new ComboBox<>();
        for (int i = 8; i <= 20; i++) {
            timeComboBox.getItems().add(String.format("%02d:00", i));
            timeComboBox.getItems().add(String.format("%02d:30", i));
        }
        timeComboBox.setMaxWidth(Double.MAX_VALUE);

        TextArea notes = new TextArea();
        notes.setPromptText("Note aggiuntive...");
        notes.setPrefRowCount(3);

        Button confirmButton = new Button("Conferma Prenotazione");
        confirmButton.setMaxWidth(Double.MAX_VALUE);
        confirmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        confirmButton.setOnAction(e -> handleBooking(trainerName, datePicker.getValue(),
                timeComboBox.getValue(), notes.getText()));

        bookingFormContainer.getChildren().addAll(
                title,
                new Label("Data:"),
                datePicker,
                new Label("Ora:"),
                timeComboBox,
                new Label("Note:"),
                notes,
                confirmButton
        );
    }

    // gestione prenotazione
    private void handleBooking(String trainerName, LocalDate date, String time, String notes) {
        if (date == null || time == null || time.isEmpty()) {
            showAlert("Errore", "Per favore seleziona data e ora");
            return;
        }

        // Qui implementa la logica per salvare la prenotazione
        showAlert("Conferma", String.format("Prenotazione confermata!\n\nPersonal Trainer: %s\nData: %s\nOra: %s",
                trainerName, date, time));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}