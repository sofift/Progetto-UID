package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.Client;
import it.unical.informatica.progettouid.model.ClientSession;
import it.unical.informatica.progettouid.util.ThemeManager;
import it.unical.informatica.progettouid.view.AlertManager;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import it.unical.informatica.progettouid.view.SceneHandlerPrimaPagina;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class ImpostazioniClientController {
    @FXML private Label cognomeLabel;
    @FXML private Label dataNascitaLabel;
    @FXML private Label emailLabel;
    @FXML private Button logoutButton;
    @FXML private Label nomeLabel;
    @FXML private PasswordField passwordField;
    private Client client = null;

    public void initialize() {
        client = ClientSession.getInstance().getCurrentClient();
        nomeLabel.setText("Nome: " + client.nome() );
        cognomeLabel.setText("Cognome: " + client.cognome() );
        dataNascitaLabel.setText("Data di nascita: " + client.dataNascita());
        emailLabel.setText("Email: " + client.email() );
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

    /*
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

    public void logout(ActionEvent actionEvent) throws Exception {
        try {
            SceneHandlerClient.getInstance().logout();
        } catch (Exception e) {
            AlertManager al = new AlertManager(Alert.AlertType.ERROR, "Errore", "Errore durante il logout", "Si è verificato un errore durante il logout. Riprova");
            al.showAndWait();
        }}

     */
}
