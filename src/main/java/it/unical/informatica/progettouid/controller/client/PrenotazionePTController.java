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

    private VBox createTrainerCard(PersonalTrainer trainer) {
        VBox card = new VBox();

        Label nameLabel = new Label(STR."\{trainer.nome()} \{trainer.nome()}");

        Text specLabel = new Text(trainer.specializzazione());

        Button bookButton = new Button("Prenota sessione");
        bookButton.setOnAction(e -> showBookingForm(trainer));


        card.getChildren().addAll(nameLabel, specLabel, bookButton);
        return card;
    }

    // mostra form laterale per la prenotazione
    private void showBookingForm(PersonalTrainer trainer) {
        bookingFormContainer.getChildren().clear();

        Label title = new Label(STR."Prenota sessione con \{trainer.nome()}");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setMaxWidth(Double.MAX_VALUE);

        TextField time = new TextField();
        time.setPrefWidth(50);

        TextArea notes = new TextArea();
        notes.setPromptText("Note aggiuntive...");
        notes.setPrefRowCount(3);

        Button confirmButton = new Button("Conferma Prenotazione");
        confirmButton.setMaxWidth(Double.MAX_VALUE);
        confirmButton.setOnAction(e -> handleBooking(trainer.id(), datePicker.getValue(),
                time.getText(), notes.getText()));

        bookingFormContainer.getChildren().addAll(
                title,
                new Label("Data:"),
                datePicker,
                new Label("Ora:"),
                time,
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
                String message = "Hai una nuova prenotazione dal cliente " + currentClient.nome() + " " + currentClient.cognome();
                if(!notes.isEmpty()) {
                    message += " Richiesta: " + notes;
                }
                Task<Void> task1 = DBConnection.getInstance().insertNotifyTrainer(trainerID, message);
                Thread thread1 = new Thread(task1);
                thread1.setDaemon(true);
                thread1.start();

                task1.setOnSucceeded(e->{
                    AlertManager succ = new AlertManager(Alert.AlertType.CONFIRMATION, "Conferma", null, "Prenotazione inviata al personal trainer, riceverai una notifica di conferma non appena possibile");
                    succ.display();
                });

                task1.setOnFailed(e -> {
                    task1.getException().printStackTrace();
                });

            }


        });

        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            ex.printStackTrace();
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

        if (date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now())) {
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
                case "impostazioniClient":
                    SceneHandlerClient.getInstance().setImpostazioniView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}