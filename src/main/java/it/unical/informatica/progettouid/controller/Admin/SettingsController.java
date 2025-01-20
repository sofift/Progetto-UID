package it.unical.informatica.progettouid.controller.Admin;

import it.unical.informatica.progettouid.model.AdminSession;
import it.unical.informatica.progettouid.view.SceneHandlerAdmin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML private RadioButton lightTheme;
    @FXML private RadioButton darkTheme;
    @FXML private Button logoutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupThemeToggle();
    }

    private void setupThemeToggle() {
        lightTheme.setSelected(true); // Default theme

        lightTheme.setOnAction(e -> applyTheme("light"));
        darkTheme.setOnAction(e -> applyTheme("dark"));
    }

    private void applyTheme(String theme) {
        // Implementa la logica per cambiare il tema
        System.out.println("Changing theme to: " + theme);
    }

    @FXML
    private void handleChangePassword() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Cambio Password");
        dialog.setHeaderText("Inserisci la nuova password");

        PasswordField currentPassword = new PasswordField();
        PasswordField newPassword = new PasswordField();
        PasswordField confirmPassword = new PasswordField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        grid.add(new Label("Password attuale:"), 0, 0);
        grid.add(currentPassword, 1, 0);
        grid.add(new Label("Nuova password:"), 0, 1);
        grid.add(newPassword, 1, 1);
        grid.add(new Label("Conferma password:"), 0, 2);
        grid.add(confirmPassword, 1, 2);

        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Conferma", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeOk) {
            if (!newPassword.getText().equals(confirmPassword.getText())) {
                showAlert("Errore", "Le password non coincidono");
                return;
            }
            // Implementa la logica per il cambio password
            System.out.println("Changing password...");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            SceneHandlerAdmin.getInstance().logout();
        } catch (Exception e) {
            showAlert("Errore", "Errore durante il logout: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Metodi di navigazione
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