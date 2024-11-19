//package it.unical.informatica.progettouid.view;
//
//import it.unical.informatica.progettouid.FlexFit;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.BorderPane;
//import javafx.stage.Stage;
//import org.kordamp.ikonli.javafx.FontIcon;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Objects;
//
//public class SceneHandler {
//    private static final String RESOURCE_PATH = "it/unical/informatica/progettouid";
//    private Scene scene;
//    private Stage stage;
//    private String theme = "dark";
//    private final Alert alert;
//    private static SceneHandler instance = null;
//    public String username;
//    private double windowWidth = 600;
//    private double windowHeight = 400;
//
//    SceneHandler() {
//        this.alert = new Alert(AlertType.INFORMATION);
//    }
//
//    public void init(Stage primaryStage) throws Exception {
//        if (this.stage == null) {
//            this.stage = primaryStage;
//            FXMLLoader fxmlLoader = new FXMLLoader(FlexFit.class.getResource("/fxml/dashboardClient.fxml"));
//            this.scene = new Scene((Parent)fxmlLoader.load(), 600.0, 400.0);
//            this.stage.setScene(this.scene);
//            this.stage.setTitle("Spotify");
//            this.stage.setResizable(false);
//            /*this.stage.setOnCloseRequest((e) -> {
//                ((AccessiController)fxmlLoader.getController()).shutdown();
//            });*/
//            this.stage.show();
//        }
//    }
//
//    public static SceneHandler getInstance() {
//        if (instance == null) {
//            instance = new SceneHandler();
//        }
//        return instance;
//    }
//
//    public void setInitScene() throws Exception {
//        this.setCurrentRoot("login.fxml");  // da creare
//        this.stage.hide();
//        this.stage.setResizable(false);
//        this.stage.setWidth(300.0);
//        this.stage.setHeight(200.0);
//        this.stage.show();
//    }
//
//    // Metodo per salvare le dimensioni della finestra
//
//    /*private void saveWindowSize() {
//        this.windowWidth = this.stage.getWidth();
//        this.windowHeight = this.stage.getHeight();
//    }
//
//    // Metodo per ripristinare le dimensioni della finestra
//    private void restoreWindowSize() {
//        this.stage.setWidth(this.windowWidth);
//        this.stage.setHeight(this.windowHeight);
//    } */
//
//    public void setHomeScene() throws Exception {
//        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/dashboardClient.fxml"));
//        this.scene = new Scene((Parent)loader.load(), 1000, 600);
//        this.stage.centerOnScreen();
//        this.stage.setResizable(true);
//        this.stage.setWidth(1000);
//        this.stage.setHeight(600);
//        this.stage.setScene(scene);
//        this.stage.show();
//
//    }
//
//    public void setAttivitaClientScene() throws Exception {
//        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/attivitaClient.fxml"));
//        Scene scene = new Scene((Parent)loader.load());
//        this.stage.setResizable(true);
//        this.stage.setScene(scene);
//        this.setCurrentRoot("/fxml/homeDefault.fxml");
//        this.stage.show();
//    }
//
//    public void setPrenotazionePTScene() throws Exception{
//        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/prenotazionePT.fxml"));
//        Scene scene = new Scene((Parent)loader.load());
//    }
//
//    public void setSchedaClientScene() throws Exception{
//        FXMLLoader loader = new FXMLLoader(FlexFit.class.getResource("/fxml/visualizzazionePlaylist.fxml"));
//        AnchorPane pane = loader.load();
//        // Verifica che il controller sia stato inizializzato
//        VisualizzazionePlaylistController controller = loader.getController();
//        if (controller == null) {
//            return;
//        }
//        // Ottiene il root della scena, che è un AnchorPane
//        AnchorPane root = (AnchorPane) this.scene.getRoot();
//
//        // Trova il BorderPane all'interno dell'AnchorPane
//        BorderPane borderPane = null;
//        for (Node node : root.getChildren()) {
//            if (node instanceof BorderPane) {
//                borderPane = (BorderPane) node;
//                break;  // BorderPane trovato, esce dal ciclo
//            }
//        }
//
//        // Verifica se il BorderPane è stato trovato
//        if (borderPane != null) {
//            // Imposta il nuovo AnchorPane al centro del BorderPane
//
//            // Aggiungi contenuto esistente del bottom (se necessario)
//            Node bottomContent = borderPane.getBottom();  // Conserva il contenuto esistente
//
//            borderPane.setCenter(pane);  // Imposta il centro con il nuovo contenuto
//            borderPane.setBottom(bottomContent);  // Riporta il contenuto del bottom
//
//            borderPane.layout();  // Aggiorna il layout
//        } else {
//            System.out.println("Errore: BorderPane non trovato.");
//        }
//    }
//
//    public void changeTheme() {
//        if ("dark".equals(this.theme)) {
//            this.theme = "light";
//        } else {
//            this.theme = "dark";
//        }
//
//        this.changedTheme();
//    }
//
//    private List<String> loadCSS() {
//        List<String> resources = new ArrayList();
//        Iterator var2 = List.of("/it/unical/spotify/css/" + this.theme + ".css", "/it/unical/spotify/css/fonts.css", "/it/unical/spotify/css/style.css").iterator();
//
//        while(var2.hasNext()) {
//            String style = (String)var2.next();
//            String resource = ((URL)Objects.requireNonNull(SceneHandler.class.getResource(style))).toExternalForm();
//            resources.add(resource);
//        }
//
//        return resources;
//    }
//
//    private void setCSSForScene(Scene scene) {
//        Objects.requireNonNull(scene, "Scene cannot be null");
//        List<String> resources = this.loadCSS();
//        scene.getStylesheets().clear();
//        Iterator var3 = resources.iterator();
//
//        while(var3.hasNext()) {
//            String resource = (String)var3.next();
//            scene.getStylesheets().add(resource);
//        }
//
//    }
//
//    private void setCSSForAlert(Alert alert) {
//        Objects.requireNonNull(alert, "Alert cannot be null");
//        List<String> resources = this.loadCSS();
//        alert.getDialogPane().getStylesheets().clear();
//        Iterator var3 = resources.iterator();
//
//        while(var3.hasNext()) {
//            String resource = (String)var3.next();
//            alert.getDialogPane().getStylesheets().add(resource);
//        }
//
//    }
//
//    private void changedTheme() {
//        this.setCSSForScene(this.scene);
//        this.setCSSForAlert(this.alert);
//    }
//
//    public boolean isDarkTheme() {
//        return "dark".equals(this.theme);
//    }
//
//    private void setCurrentRoot(String filename) throws Exception {
//        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/" + filename));
//        this.scene.setRoot((Parent)loader.load());
//    }
//
//    public void showError(String message) {
//        FontIcon icon = new FontIcon("mdi-alert");
//        icon.getStyleClass().add("icons-color");
//        icon.setIconSize(40);
//        this.alert.setGraphic(icon);
//        this.alert.setTitle("Error");
//        this.alert.setHeaderText("");
//        this.alert.setContentText(message);
//        this.alert.show();
//    }
//
//    public void showInfo(String message) {
//        FontIcon icon = new FontIcon("mdi2i-information-outline");
//        icon.getStyleClass().add("icons-color");
//        icon.setIconSize(40);
//        this.alert.setGraphic(icon);
//        this.alert.setTitle("Info");
//        this.alert.setHeaderText("");
//        this.alert.setContentText(message);
//        this.alert.show();
//    }
//
//
//}
