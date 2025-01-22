package it.unical.informatica.progettouid.controller.Admin;

import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.model.PersonalTrainer;
import it.unical.informatica.progettouid.model.NuovoCorso;
import it.unical.informatica.progettouid.view.SceneHandlerAdmin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AggiungiCorsoController implements Initializable {
    @FXML private TextField nomeCorso;
    @FXML private ComboBox<String> tipoCorso;
    @FXML private Spinner<Integer> durata;
    @FXML private Spinner<Integer> maxPartecipanti;
    @FXML private ComboBox<PersonalTrainer> personalTrainer;
    @FXML private TextArea descrizione;

    @FXML private CheckBox lunedi;
    @FXML private CheckBox martedi;
    @FXML private CheckBox mercoledi;
    @FXML private CheckBox giovedi;
    @FXML private CheckBox venerdi;
    @FXML private CheckBox sabato;
    @FXML private CheckBox domenica;

    @FXML private ComboBox<String> orarioInizio;
    @FXML private Button annullaButton;
    @FXML private Button salvaCorso;

    private final DBConnection dbConnection;

    public AggiungiCorsoController() {
        this.dbConnection = DBConnection.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupSpinners();
        setupTrainerComboBox();
        setupOrarioInizioComboBox();
        setupButtons();
        setupValidation();
    }

    private void setupSpinners() {
        // Setup durata spinner (30-180 minutes)
        SpinnerValueFactory<Integer> durataFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(30, 180, 60, 15);
        durata.setValueFactory(durataFactory);

        // Setup max partecipanti spinner (5-50 people)
        SpinnerValueFactory<Integer> maxPartFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 50, 15, 5);
        maxPartecipanti.setValueFactory(maxPartFactory);
    }

    private void setupTrainerComboBox() {
        // Load personal trainers from database
        dbConnection.getAllPt().setOnSucceeded(event -> {
            @SuppressWarnings("unchecked")
            List<PersonalTrainer> trainers = (List<PersonalTrainer>) event.getSource().getValue();
            personalTrainer.getItems().addAll(trainers);
        });

        // Execute the task to load trainers
        Thread thread = new Thread(dbConnection.getAllPt());
        thread.setDaemon(true);
        thread.start();

        // Setup display format for trainer names
        personalTrainer.setConverter(new StringConverter<PersonalTrainer>() {
            @Override
            public String toString(PersonalTrainer trainer) {
                if (trainer == null) return "";
                return trainer.nome() + " " + trainer.cognome();
            }

            @Override
            public PersonalTrainer fromString(String string) {
                return null; // Not needed for this implementation
            }
        });
    }

    private void setupOrarioInizioComboBox() {
        // Add time slots from 06:00 to 22:00 with 30-minute intervals
        List<String> timeSlots = new ArrayList<>();
        LocalTime time = LocalTime.of(6, 0);
        LocalTime endTime = LocalTime.of(22, 0);

        while (!time.isAfter(endTime)) {
            timeSlots.add(time.toString());
            time = time.plusMinutes(30);
        }

        orarioInizio.getItems().addAll(timeSlots);
    }

    private void setupButtons() {
        annullaButton.setOnAction(e -> handleAnnulla());
        //salvaCorso.setOnAction(e -> handleSalva());
    }

    private void setupValidation() {
        // Add listeners for required fields
        nomeCorso.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        tipoCorso.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
        personalTrainer.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
        orarioInizio.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
    }

    private boolean validateForm() {
        boolean isValid = !nomeCorso.getText().trim().isEmpty() &&
                tipoCorso.getValue() != null &&
                personalTrainer.getValue() != null &&
                orarioInizio.getValue() != null &&
                getSelectedDays().size() > 0;

        salvaCorso.setDisable(!isValid);
        return isValid;
    }

    private List<String> getSelectedDays() {
        List<String> selectedDays = new ArrayList<>();
        if (lunedi.isSelected()) selectedDays.add("lunedi");
        if (martedi.isSelected()) selectedDays.add("martedi");
        if (mercoledi.isSelected()) selectedDays.add("mercoledi");
        if (giovedi.isSelected()) selectedDays.add("giovedi");
        if (venerdi.isSelected()) selectedDays.add("venerdi");
        if (sabato.isSelected()) selectedDays.add("sabato");
        if (domenica.isSelected()) selectedDays.add("domenica");
        return selectedDays;
    }

    @FXML
    /*private void handleSalva() {
        if (!validateForm()) {
            showError("Compila tutti i campi obbligatori");
            return;
        }

        try {
            NuovoCorso corso = new NuovoCorso();
            corso.setNome(nomeCorso.getText().trim());
            corso.setDescrizione(descrizione.getText().trim());
            corso.setDurata(durata.getValue());
            PersonalTrainer selectedTrainer = personalTrainer.getValue();
            corso.setPt(selectedTrainer.nome() + " " + selectedTrainer.cognome());
            corso.setGiorni(getSelectedDays());
            corso.setOrarioInizio(LocalTime.parse(orarioInizio.getValue()));

            // Calculate end time based on duration
            LocalTime startTime = corso.getOrarioInizio();
            LocalTime endTime = startTime.plusMinutes(corso.getDurata());
            corso.setOrarioFine(endTime);

            corso.setMaxPartecipanti(maxPartecipanti.getValue());

            var task = dbConnection.insertCorso(corso);

            task.stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == javafx.concurrent.Worker.State.SUCCEEDED) {
                    Boolean result = task.getValue();
                    if (result != null && result) {
                        showSuccess("Corso aggiunto con successo");
                        closeWindow();
                    } else {
                        showError("Errore durante il salvataggio del corso");
                    }
                } else if (newValue == javafx.concurrent.Worker.State.FAILED) {
                    if (task.getException() != null) {
                        showError("Errore durante il salvataggio del corso: " +
                                task.getException().getMessage());
                    } else {
                        showError("Errore durante il salvataggio del corso");
                    }
                }
            });

            // Execute the task
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();

        } catch (Exception e) {
            showError("Errore durante il salvataggio del corso: " + e.getMessage());
        }
    }*/

    private void handleAnnulla() {
        if (hasUnsavedChanges()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma");
            alert.setHeaderText("Vuoi davvero annullare?");
            alert.setContentText("Le modifiche non salvate andranno perse.");

            if (alert.showAndWait().get() == ButtonType.OK) {
                closeWindow();
            }
        } else {
            closeWindow();
        }
    }

    private boolean hasUnsavedChanges() {
        return !nomeCorso.getText().trim().isEmpty() ||
                tipoCorso.getValue() != null ||
                !descrizione.getText().trim().isEmpty() ||
                getSelectedDays().size() > 0;
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) annullaButton.getScene().getWindow();
        stage.close();
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
