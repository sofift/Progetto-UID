package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.Client;
import it.unical.informatica.progettouid.model.ClientSession;
import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.model.PersonalTrainer;
import it.unical.informatica.progettouid.view.AlertManager;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrenotazionePTController{
    @FXML
    private VBox trainerListContainer;
    @FXML
    private VBox bookingFormContainer;
    private Client currentClient;

    public void initialize() {
        currentClient = ClientSession.getInstance().getCurrentClient();
        loadTrainers();
    }

    // caricamento dei trainer
    private void loadTrainers() {
        Task<List<PersonalTrainer>> task = DBConnection.getInstance().getAllPt();

        task.setOnSucceeded(event -> {
            trainerListContainer.getChildren().clear();
            List<PersonalTrainer> trainers = task.getValue();
            if (trainers.isEmpty()) {
                AlertManager err = new AlertManager(Alert.AlertType.ERROR, "Errore", null, "Errore nel caricamento dei trainer, riprova più tardi");
                err.display();
            } else {
                displayPersonal(trainers);
            }
        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento dei personal(listVirw): " + task.getException().getMessage());
        });

        new Thread(task).start();

    }

    private void displayPersonal(List<PersonalTrainer> trainers) {
        for(PersonalTrainer trainer : trainers) {
            trainerListContainer.getChildren().add(createTrainerCard(trainer));
        }
    }

    // creazione sezione per ogni trainer --> il CSS è da mettere nel foglio di stile
    private VBox createTrainerCard(PersonalTrainer trainer) {
        VBox card = new VBox();
//        card.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; " +
//                "-fx-border-radius: 5; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
//        card.setPadding(new Insets(15));
//        card.setSpacing(5);

        Label nameLabel = new Label(STR."\{trainer.getNome()} \{trainer.getNome()}");
//        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Text specLabel = new Text(trainer.getSpecializzazione());

        Button bookButton = new Button("Prenota sessione");
//        bookButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        bookButton.setOnAction(e -> showBookingForm(trainer));


        card.getChildren().addAll(nameLabel, specLabel, bookButton);
        return card;
    }

    // mostra form laterale per la prenotazione
    private void showBookingForm(PersonalTrainer trainer) {
        bookingFormContainer.getChildren().clear();         // pulisce la aprte destra del border pane ogni votla che si preme sul button prenotazione

        Label title = new Label("Prenota sessione con " + trainer.getNome());
        //title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> timeComboBox = new ComboBox<>();
//        for (int i = 8; i <= 20; i++) {
//            timeComboBox.getItems().add(String.format("%02d:00", i));
//            timeComboBox.getItems().add(String.format("%02d:30", i));
//        }
//        timeComboBox.setMaxWidth(Double.MAX_VALUE);

        TextArea notes = new TextArea();
        notes.setPromptText("Note aggiuntive...");
        notes.setPrefRowCount(3);

        Button confirmButton = new Button("Conferma Prenotazione");
        confirmButton.setMaxWidth(Double.MAX_VALUE);
//        confirmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        confirmButton.setOnAction(e -> handleBooking(trainer.getId(), datePicker.getValue(),
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
    private void handleBooking(int trainerID, LocalDate date, String time, String notes) {
        if (!validateBookingInput(date, time)) {
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String dateString = date.format(formatter);

        Task<Boolean> task = DBConnection.getInstance().insertPrenotazionePT(trainerID, dateString, time, notes);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(event -> {
            if(task.getValue()){
                String message = STR."Hai una nuova prenotazione dal cliente\{currentClient.getNome()}\{currentClient.getCognome()}.";
                if(!notes.isEmpty()) {
                    message += STR."""
                    Richiesta:\{notes}""";
                }
                Task<Void> task1 = DBConnection.getInstance().insertNotifyTrainer(trainerID, message);
            }

            AlertManager succ = new AlertManager(Alert.AlertType.CONFIRMATION, "Conferma", null, "Prenotazione inviata al personal trainer, riceverai una notifica di conferma non appena possibile");
            succ.display();
        });

        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            ex.printStackTrace();  // Stampa l'errore completo in console

            AlertManager errore = new AlertManager(Alert.AlertType.ERROR, "Errore prenotazione", null, STR."Si è verificato un errore durante la prenotazione: \{ex.getMessage()}");
            errore.display();

        });

    }

    private boolean validateBookingInput(LocalDate date, String time) {
        if (date == null || time == null || time.isEmpty()) {
            AlertManager er = new AlertManager(Alert.AlertType.WARNING, "Attenzione", "Dati mancanti", "Seleziona data e ora per la prenotazione." );
            er.display();
            return false;
        }

        if (date.isBefore(LocalDate.now())) {
            AlertManager er = new AlertManager(Alert.AlertType.WARNING, "Data non valida", null, "Seleziona una data futura.");
            er.display();
            return false;
        }

        return true;
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