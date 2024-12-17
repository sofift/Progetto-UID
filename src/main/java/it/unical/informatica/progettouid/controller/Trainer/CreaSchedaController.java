package it.unical.informatica.progettouid.controller.Trainer;

import it.unical.informatica.progettouid.model.*;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class CreaSchedaController {
    @FXML private VBox vboxCenter;
    @FXML private ComboBox<Client> clienteComboBox;
    @FXML private VBox schedaInfoPanel;
    @FXML private ScrollPane aggiungiEsercizioPanel;
    @FXML private ComboBox<String> esercizioComboBox;
    @FXML private Spinner<Integer> serieSpinner;
    @FXML private Spinner<Integer> ripetizioniSpinner;
    @FXML private Spinner<Integer> recuperoSpinner;
    @FXML private TextArea noteEsercizioTextArea;
    @FXML private DatePicker dataInizioPicker;
    @FXML private DatePicker dataFinePicker;
    @FXML private TextField durataSettField;


    // TODO: implementare la logica di visualizzazione delle tabelle se il client ha una scheda o meno

    @FXML
    private void initialize() {
        // Inizializzazione dei componenti
        mostraClienti();
        clienteComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                verificaSchedaClient(newSelection);  // newSelection è l'oggetto Client selezionato
            }
        });



    }

    private void loadSchedaCliente(Client client) {
        dataInizioPicker.setEditable(false);
        dataFinePicker.setEditable(false);
        durataSettField.setEditable(false);

        HBox buttons = new HBox();
        Button modifica = new Button("Modifica scheda");
        buttons.getChildren().add(modifica);
        modifica.setOnAction(event -> {modificaScheda(client);});

        TabPane weekdayTabs = new TabPane();
        String[] giorniSet = {"Lunedì, Martedì, Mercoledì, Giovedì, Venerdì, Sabato"};

        for (String day : giorniSet) {
            Tab tab = new Tab(day);
            VBox vbox = new VBox();
            TableView<Esercizio> tableView = createEsercizioTableView();

            vbox.getChildren().add(tableView);

            tab.setContent(vbox);
            weekdayTabs.getTabs().add(tab);
        }

        weekdayTabs.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null && clienteComboBox.getSelectionModel().getSelectedItem() != null) {
                String selectedDay = newTab.getText();
                loadEserciziScheda(client.getId(), selectedDay, (TableView<Esercizio>) ((VBox) newTab.getContent()).getChildren().get(0));
            }
        });

    }

    private void modificaScheda(Client client) {
        dataInizioPicker.setEditable(false);
        dataFinePicker.setEditable(false);
        durataSettField.setEditable(false);

        schedaInfoPanel.setVisible(false);
        aggiungiEsercizioPanel.setVisible(true);
    }

    private TableView<Esercizio> createEsercizioTableView() {
        TableView<Esercizio> tableView = new TableView<>();
        TableColumn<Esercizio, Number> serieColumn = new TableColumn<>("Serie");
        TableColumn<Esercizio, Number> ripetizioniColumn = new TableColumn<>("Ripetizioni");
        TableColumn<Esercizio, Number> tempoRecuperoColumn = new TableColumn<>("Tempo Recupero");
        TableColumn<Esercizio, String> noteColumn = new TableColumn<>("Note");
        TableColumn<Esercizio, String> nomeEsercizioColumn = new TableColumn<>("Esercizio");
        TableColumn<Esercizio, String> descrizioneColumn = new TableColumn<>("Descrizione");
        TableColumn<Esercizio, String> gMuscolareColumn = new TableColumn<>("Gruppo Muscolare");
        TableColumn<Esercizio, String> difficoltaColumn = new TableColumn<>("Difficoltà");
        tableView.getColumns().addAll(serieColumn, ripetizioniColumn, tempoRecuperoColumn, noteColumn, nomeEsercizioColumn, descrizioneColumn, gMuscolareColumn, difficoltaColumn);

        serieColumn.setCellValueFactory(cellData -> cellData.getValue().nSerieProperty());
        ripetizioniColumn.setCellValueFactory(cellData -> cellData.getValue().nRipetizioniProperty());
        tempoRecuperoColumn.setCellValueFactory(cellData -> cellData.getValue().tmpRecuperoProperty());
        noteColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());
        nomeEsercizioColumn.setCellValueFactory(cellData -> cellData.getValue().nomeEsercProperty());
        descrizioneColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());
        gMuscolareColumn.setCellValueFactory(cellData -> cellData.getValue().gMuscolareProperty());
        difficoltaColumn.setCellValueFactory(cellData -> cellData.getValue().diffProperty());

        return tableView;
    }

    private void verificaSchedaClient(Client client) {
        Task<Boolean> task = DBConnection.getInstance().clientHaUnaScheda(client.getId());
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(e -> {
            Boolean result = task.getValue();
            System.out.println(result);
            if(result){
                loadSchedaCliente(client);
            }
            else{
                VBox info = new VBox(10);
                info.setAlignment(Pos.CENTER);
                Label nota = new Label(STR."\{client.getNome()} non ha ancora una scheda, creala subito");
                Button creaScheda = new Button("Crea scheda");

                creaScheda.setOnAction(event-> {
                    schedaInfoPanel.setVisible(false);
                    aggiungiEsercizioPanel.setVisible(true);
                });

                info.getChildren().addAll(nota, creaScheda);
                vboxCenter.getChildren().addAll(info);
            }
        });

        task.setOnFailed(e -> {
            System.out.println(task.getException());
        });

    }


    private void loadEserciziScheda(int clientId, String selectedDay, TableView<Esercizio> esercizioTableView) {

        Task<ObservableList<Esercizio> > task = DBConnection.getInstance().getEserciziGiorno(clientId, selectedDay);

        task.setOnSucceeded(event -> {
            ObservableList<Esercizio> esercizi = task.getValue();

            esercizioTableView.setItems(esercizi);

        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento delle informazioni: " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    private void mostraClienti() {
        Task<List<Client>> task = DBConnection.getInstance().getAllClient();

        task.setOnSucceeded(event -> {
            clienteComboBox.getItems().clear();
            List<Client> clients = task.getValue();
            if (clients.isEmpty()) {
                showAlertSucc("Errore", "Erorre nel caricamento dei client");
            } else {
                for (Client client : clients) {
                    clienteComboBox.getItems().add(client);
                }
            }
        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento dei personal(listVirw): " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    public void formAggiungiEsercizio(ActionEvent actionEvent) {
        schedaInfoPanel.setVisible(false);
        aggiungiEsercizioPanel.setVisible(true);
    }


    // TODO: logica per inserire i nuovi esercizi al database, devo collegare gli esercizi delle scheda con i dettagli della tab Esercizi
    @FXML
    private void aggiungiEsercizio() {
        String nomeEsercizio = esercizioComboBox.getValue();
        if (nomeEsercizio == null || nomeEsercizio.trim().isEmpty()) {
            mostraErrore("Devi selezionare o inserire un esercizio!");
            return;
        }

        /*Esercizio nuovoEsercizio = new Esercizio(

                serieSpinner.getValue(),
                ripetizioniSpinner.getValue(),
                STR."\{recuperoSpinner.getValue()} sec",
                noteEsercizioTextArea.getText(),
                nomeEsercizio,


        );

        eserciziTable.getItems().add(nuovoEsercizio);
        pulisciFormEsercizio();
        mostraPannelloPrincipale();*/
    }

    @FXML
    private void salvaScheda() {
        // Salvare la scheda nel database
    }

    @FXML
    private void anteprimaPDF() {
        // Generare anteprima PDF della scheda
    }

    private void showAlertSucc(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void annullaAggiuntaEsercizio(ActionEvent actionEvent) {
        pulisciFormEsercizio();
        mostraPannelloPrincipale();
    }

    private void pulisciFormEsercizio() {
        esercizioComboBox.setValue(null);
        serieSpinner.getValueFactory().setValue(3);
        ripetizioniSpinner.getValueFactory().setValue(12);
        recuperoSpinner.getValueFactory().setValue(90);
        noteEsercizioTextArea.clear();
    }

    private void mostraPannelloPrincipale() {
        schedaInfoPanel.setVisible(true);
        aggiungiEsercizioPanel.setVisible(false);
    }

    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

}




