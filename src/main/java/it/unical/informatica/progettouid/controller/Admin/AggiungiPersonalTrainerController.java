package it.unical.informatica.progettouid.controller.Admin;

import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.model.PersonalTrainer;
import it.unical.informatica.progettouid.util.PasswordUtils;
import it.unical.informatica.progettouid.view.SceneHandlerAdmin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AggiungiPersonalTrainerController implements Initializable {
    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private DatePicker dataNascitaPicker;
    @FXML private ComboBox<String> specializzazioneCombo;
    @FXML private TextField emailField;
    @FXML private TextField telefonoField;
    @FXML private Button annullaButton;
    @FXML private Button salvaButton;
    @FXML private TextField passwordField;
    @FXML private Button copyPasswordButton;

    private String generatedPassword;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9]{10,13}$");

    private final DBConnection dbConnection;

    public AggiungiPersonalTrainerController() {
        this.dbConnection = DBConnection.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupSpecializzazioni();
        setupValidation();
        setupButtons();
    }


    private void copyPasswordToClipdoard() {
        if (generatedPassword != null && generatedPassword.isEmpty()) {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(generatedPassword);
            clipboard.setContent(content);

            showSuccess("Password copiata negli appunti");
        }

    }

    private void setupSpecializzazioni() {
        specializzazioneCombo.getItems().addAll(Arrays.asList(
                "Fitness",
                "Yoga",
                "Pilates",
                "Personal Training",
                "Crossfit",
                "Bodybuilding",
                "Functional Training",
                "Riabilitazione Sportiva"
        ));
    }

    private void setupValidation() {
        // Add listeners for real-time validation
        nomeField.textProperty().addListener((obs, old, newValue) -> validateForm());
        cognomeField.textProperty().addListener((obs, old, newValue) -> validateForm());
        dataNascitaPicker.valueProperty().addListener((obs, old, newValue) -> validateForm());
        specializzazioneCombo.valueProperty().addListener((obs, old, newValue) -> validateForm());
        emailField.textProperty().addListener((obs, old, newValue) -> validateForm());
        telefonoField.textProperty().addListener((obs, old, newValue) -> validateForm());
    }

    private void setupButtons() {
        salvaButton.setOnAction(e -> handleSalva());
        annullaButton.setOnAction(e -> handleAnnulla());
    }

    private boolean validateForm() {
        boolean isValid = isValidName(nomeField.getText()) &&
                isValidName(cognomeField.getText()) &&
                isValidDate(dataNascitaPicker.getValue()) &&
                specializzazioneCombo.getValue() != null &&
                isValidEmail(emailField.getText()) &&
                isValidPhone(telefonoField.getText());

        if (isValid) {
            if (generatedPassword == null) {
                generatedPassword = PasswordUtils.generateSecurePassword();
                passwordField.setText(generatedPassword);
                copyPasswordButton.setDisable(false);
            }
        } else {
            passwordField.clear();
            copyPasswordButton.setDisable(true);
            generatedPassword = null;
        }

        salvaButton.setDisable(!isValid);
        return isValid;
    }

    private boolean isValidName(String name) {
        return name != null && name.trim().length() >= 2;
    }

    private boolean isValidDate(LocalDate date) {
        if (date == null) return false;
        LocalDate minDate = LocalDate.now().minusYears(65);
        LocalDate maxDate = LocalDate.now().minusYears(18);
        return !date.isBefore(minDate) && !date.isAfter(maxDate);
    }

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.replaceAll("\\s+", "")).matches();
    }

    private void handleSalva() {
        if (!validateForm()) {
            showError("Verifica i dati inseriti");
            return;
        }

        try {
            // Create a PersonalTrainer record with the generated password
            PersonalTrainer trainer = new PersonalTrainer(
                    0, // ID will be set by database
                    nomeField.getText().trim(),
                    cognomeField.getText().trim(),
                    dataNascitaPicker.getValue().format(DateTimeFormatter.ISO_DATE),
                    specializzazioneCombo.getValue(),
                    emailField.getText().trim(),
                    generatedPassword,
                    telefonoField.getText().trim()
            );

            var task = dbConnection.insertTrainer(
                    trainer.nome(),
                    trainer.cognome(),
                    trainer.dataNascita(),
                    trainer.specializzazione(),
                    trainer.email(),
                    trainer.password(),
                    trainer.telefono()
            );

            task.stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == javafx.concurrent.Worker.State.SUCCEEDED) {
                    Boolean result = task.getValue();
                    if (result != null && result) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Personal Trainer Aggiunto");
                        alert.setHeaderText("Personal Trainer aggiunto con successo");
                        alert.setContentText("""
                            Password generata: %s
                            
                            IMPORTANTE: Conserva questa password in modo sicuro.
                            Non sarà più possibile visualizzarla dopo la chiusura di questa finestra.
                            """.formatted(generatedPassword));

                        alert.showAndWait();
                        resetForm();
                    } else {
                        showError("Errore durante il salvataggio");
                    }
                } else if (newValue == javafx.concurrent.Worker.State.FAILED) {
                    showError("Errore durante il salvataggio: " +
                            (task.getException() != null ? task.getException().getMessage() : "Errore sconosciuto"));
                }
            });

            // Execute the task
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();

        } catch (Exception e) {
            showError("Errore: " + e.getMessage());
        }
    }

    private void resetForm() {
        nomeField.clear();
        cognomeField.clear();
        dataNascitaPicker.setValue(null);
        specializzazioneCombo.setValue(null);
        emailField.clear();
        telefonoField.clear();
        passwordField.clear();
        generatedPassword = null;
        copyPasswordButton.setDisable(true);
    }

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
        return !nomeField.getText().trim().isEmpty() ||
                !cognomeField.getText().trim().isEmpty() ||
                dataNascitaPicker.getValue() != null ||
                specializzazioneCombo.getValue() != null ||
                !emailField.getText().trim().isEmpty() ||
                !telefonoField.getText().trim().isEmpty();
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
