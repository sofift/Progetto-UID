package it.unical.informatica.progettouid.controller.client;
import it.unical.informatica.progettouid.util.ThemeManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class ImpostazioniController {

    @FXML private Label nome;
    @FXML private Label cognome;
    @FXML private Label nascita;
    @FXML private Label email;
    @FXML private Button logOutBtn;
    @FXML private RadioButton lightTheme;
    @FXML private RadioButton darkTheme;

    private final String LIGHT_THEME_PATH = "styles/client/light/";
    private final String DARK_THEME_PATH = "styles/client/dark/";

    @FXML
    public void initialize() {
        // Setup toggle group for theme selection
        ToggleGroup themeGroup = new ToggleGroup();
        lightTheme.setToggleGroup(themeGroup);
        darkTheme.setToggleGroup(themeGroup);

        // Load current theme preference
        String currentTheme = ThemeManager.getCurrentTheme();
        if ("dark".equals(currentTheme)) {
            darkTheme.setSelected(true);
        } else {
            lightTheme.setSelected(true);
        }

        // Add theme change listeners
        lightTheme.setOnAction(e -> updateTheme("light"));
        darkTheme.setOnAction(e -> updateTheme("dark"));

        // Load user data
        loadUserData();
    }

    private void updateTheme(String theme) {
        Scene scene = nome.getScene();
        ThemeManager.setTheme(theme);

        // Update all stylesheets
        updateStylesheet(scene, "impostazioni.css");

        // Add other CSS files as needed
    }

    private void updateStylesheet(Scene scene, String cssFile) {
        String basePath = ThemeManager.getCurrentTheme().equals("dark") ? DARK_THEME_PATH : LIGHT_THEME_PATH;
        String stylesheet = getClass().getResource(basePath + cssFile).toExternalForm();

        // Remove old version and add new
        scene.getStylesheets().removeIf(style -> style.contains(cssFile));
        scene.getStylesheets().add(stylesheet);
    }

    @FXML
    private void logout() {
        // Implement logout logic
    }

    private void loadUserData() {
        // Load and display user data
    }

    @FXML
    private void onNavigationButtonClick() {
        // Handle navigation
    }
}
