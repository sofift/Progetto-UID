package it.unical.informatica.progettouid.controller.Admin;

import it.unical.informatica.progettouid.view.SceneHandlerAdmin;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.time.Period;


public class AggiungiUtenteController {

    @FXML private ProgressBar formProgress;
    @FXML private Label progressLabel;
    @FXML private TextField codiceFiscale;
    @FXML private TextField professione;
    @FXML private DatePicker certificateExpiry;
    @FXML private TextArea medicalConditions;
    @FXML private ComboBox<String> mainGoal;
    @FXML private ComboBox<String> experienceLevel;
    @FXML private TextArea fitnessNotes;
    @FXML private CheckBox ptService;
    @FXML private CheckBox lockerService;
    @FXML private CheckBox towelService;
    @FXML private CheckBox parkingService;
    @FXML private Label certificateStatus;
    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField addressField;
    @FXML private TextField cityField;
    @FXML private TextField capField;
    @FXML private TextField emergencyContactName;
    @FXML private TextField emergencyContactPhone;
    @FXML private ComboBox<String> emergencyContactRelation;
    @FXML private ComboBox<String> subscriptionType;
    @FXML private CheckBox termsCheckBox;
    @FXML private TextArea disclaimerArea;
    @FXML private Button registraNuovoUtente;
    @FXML private Button loadCertificate;


    private File selectedCertificate;
    private final DoubleProperty formCompletionProperty = new SimpleDoubleProperty(0.0);
    private Map<Control, Boolean> requiredFields = new HashMap<>();

    @FXML
    public void initialize() {
        setupInitialData();
        setupValidations();
        setupEventHandlers();
        setupFormProgress();
        loadDefaultDisclaimer();
    }

    private void setupInitialData() {
        // Setup ComboBoxes
        genderComboBox.setItems(FXCollections.observableArrayList("Maschio", "Femmina", "Altro", "Preferisco non specificare"));
        emergencyContactRelation.setItems(FXCollections.observableArrayList(
                "Genitore", "Coniuge", "Figlio/a", "Fratello/Sorella", "Amico/a", "Altro"
        ));

        // Initialize required fields mapping
        requiredFields.put(nameField, false);
        requiredFields.put(surnameField, false);
        requiredFields.put(phoneField, false);
        requiredFields.put(addressField, false);
        requiredFields.put(cityField, false);
        requiredFields.put(capField, false);
    }

    private void setupValidations() {
        // Name validation
        nameField.textProperty().addListener((obs, old, newValue) -> {
            boolean isValid = newValue != null && newValue.matches("[A-Za-z\\s]{2,30}");
            requiredFields.put(nameField, isValid);
            nameField.setStyle(isValid ? "" : "-fx-border-color: red;");
            updateFormProgress();
        });

        // Phone validation
        phoneField.textProperty().addListener((obs, old, newValue) -> {
            boolean isValid = newValue != null && newValue.matches("\\d{10}");
            requiredFields.put(phoneField, isValid);
            phoneField.setStyle(isValid ? "" : "-fx-border-color: red;");
            updateFormProgress();
        });

        // Email validation
        emailField.textProperty().addListener((obs, old, newValue) -> {
            boolean isValid = newValue != null &&
                    newValue.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}");
            emailField.setStyle(isValid ? "" : "-fx-border-color: red;");
        });

        // Codice Fiscale validation
        codiceFiscale.textProperty().addListener((obs, old, newValue) -> {
            boolean isValid = newValue != null && newValue.matches("[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]");
            codiceFiscale.setStyle(isValid ? "" : "-fx-border-color: red;");
        });

        // CAP validation
        capField.textProperty().addListener((obs, old, newValue) -> {
            boolean isValid = newValue != null && newValue.matches("\\d{5}");
            requiredFields.put(capField, isValid);
            capField.setStyle(isValid ? "" : "-fx-border-color: red;");
            updateFormProgress();
        });
    }

    private void setupEventHandlers() {
        // Handle certificate upload
        Button uploadButton = new Button("Carica Certificato");
        uploadButton.setOnAction(this::handleCertificateUpload);

        // Handle form submission
        Button submitButton = new Button("Registra");
        submitButton.setOnAction(this::handleSubmission);

        // Birthday date picker validation
        birthDatePicker.setOnAction(event -> {
            LocalDate selectedDate = birthDatePicker.getValue();
            if (selectedDate != null) {
                Period age = Period.between(selectedDate, LocalDate.now());
                if (age.getYears() < 16) {
                    showAlert("Età non valida", "L'utente deve avere almeno 16 anni.");
                    birthDatePicker.setValue(null);
                }
            }
        });

        // Certificate expiry date validation
        certificateExpiry.setOnAction(event -> {
            LocalDate expiryDate = certificateExpiry.getValue();
            if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
                showAlert("Data non valida", "La data di scadenza del certificato deve essere futura.");
                certificateExpiry.setValue(null);
            }
        });
    }

    private void setupFormProgress() {
        formProgress.progressProperty().bind(formCompletionProperty);
        updateFormProgress();
    }

    private void updateFormProgress() {
        long completedFields = requiredFields.values().stream().filter(v -> v).count();
        double progress = (double) completedFields / requiredFields.size();
        formCompletionProperty.set(progress);
        progressLabel.setText(String.format("Completamento form: %.0f%%", progress * 100));
    }

    @FXML
    private void handleCertificateUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona Certificato Medico");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            selectedCertificate = selectedFile;
            certificateStatus.setText(selectedFile.getName());
        }
    }

    @FXML
    private void handleSubmission(ActionEvent event) {
        if (!validateForm()) {
            showAlert("Form Incompleto", "Per favore completa tutti i campi obbligatori.");
            return;
        }

        if (!termsCheckBox.isSelected()) {
            showAlert("Termini e Condizioni", "Per favore accetta i termini e le condizioni.");
            return;
        }

        // Here you would typically:
        // 1. Create a User/Member object
        // 2. Save to database
        // 3. Handle any additional business logic

        showSuccess("Registrazione Completata", "Nuovo utente registrato con successo!");
        clearForm();
    }

    private boolean validateForm() {
        return requiredFields.values().stream().allMatch(valid -> valid);
    }

    private void loadDefaultDisclaimer() {
        disclaimerArea.setText(
                "Il sottoscritto dichiara di essere a conoscenza dei rischi connessi alla pratica dell'attività sportiva " +
                        "e di assumersi ogni responsabilità per eventuali infortuni o danni a persone o cose durante la permanenza " +
                        "nei locali della palestra. Dichiara inoltre di aver letto e accettato il regolamento della struttura."
        );
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccess(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearForm() {
        // Reset all form fields
        nameField.clear();
        surnameField.clear();
        phoneField.clear();
        emailField.clear();
        birthDatePicker.setValue(null);
        genderComboBox.setValue(null);
        codiceFiscale.clear();
        professione.clear();
        addressField.clear();
        cityField.clear();
        capField.clear();
        emergencyContactName.clear();
        emergencyContactPhone.clear();
        emergencyContactRelation.setValue(null);
        subscriptionType.setValue(null);
        medicalConditions.clear();
        fitnessNotes.clear();
        certificateStatus.setText("Nessun file caricato");
        selectedCertificate = null;
        termsCheckBox.setSelected(false);

        // Reset services
        ptService.setSelected(false);
        lockerService.setSelected(false);
        towelService.setSelected(false);
        parkingService.setSelected(false);

        // Reset progress
        requiredFields.replaceAll((k, v) -> false);
        updateFormProgress();
    }

    // Navigation methods
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
    private void navigateToBilling() throws Exception {
        SceneHandlerAdmin.getInstance().setBilling();
    }
}