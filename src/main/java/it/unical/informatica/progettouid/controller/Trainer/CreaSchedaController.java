package it.unical.informatica.progettouid.controller.Trainer;

import it.unical.informatica.progettouid.model.*;
import it.unical.informatica.progettouid.view.SceneHandlerPT;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class CreaSchedaController {
    @FXML private VBox vboxCenter;
    @FXML private ComboBox<Client> clienteComboBox;
    @FXML private VBox schedaInfoPanel;
    @FXML private ScrollPane aggiungiEsercizioPanel;
    @FXML private TextField esercizioTextField;
    @FXML private TextField giornoField;
    @FXML private Spinner<Integer> serieSpinner;
    @FXML private Spinner<Integer> ripetizioniSpinner;
    @FXML private Spinner<Integer> recuperoSpinner;
    @FXML private TextArea noteEsercizioTextArea;
    @FXML private DatePicker dataInizioPicker;
    @FXML private DatePicker dataFinePicker;
    @FXML private TextField gruppoMuscTextfield;
    @FXML private FlowPane flowpaneScheda;
    private Client selectedClient = null;



    // TODO: implementare la logica di visualizzazione delle tabelle se il client ha una scheda o meno

    @FXML
    private void initialize() {
        mostraClienti();
        clienteComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedClient = newSelection;
                verificaSchedaClient(newSelection);
            }
        });

    }

    private void loadSchedaCliente(Client client) {
        dataInizioPicker.setEditable(false);
        dataFinePicker.setEditable(false);

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
                creaSchedaClient(client);
            }
        });

        task.setOnFailed(e -> {
            System.out.println(task.getException());
        });

    }

    private void creaSchedaClient(Client client) {
        VBox info = new VBox(10);
        info.setAlignment(Pos.CENTER);
        Label nota = new Label(STR."\{client.getNome()} non ha ancora una scheda, creala subito");
        Button creaScheda = new Button("Crea scheda");
        info.getChildren().addAll(nota, creaScheda);
        vboxCenter.getChildren().addAll(info);

        creaScheda.setOnAction(event-> {
            creaScheda.setDisable(true);
            schedaInfoPanel.setVisible(false);
            aggiungiEsercizioPanel.setVisible(true);
            Label ob = new Label("Obiettivi");
            TextField obiettivi = new TextField();
            Label ng = new Label("Note generali:");
            TextField noteGenerali = new TextField();
            Label SuggAl = new Label("Suggerimenti alimentari");
            TextField suggerimentiAlimentari = new TextField();
            Button crea = new Button("Crea");
            crea.setOnAction(ev -> {
                if(dataFinePicker.getValue() == null || dataInizioPicker.getValue() == null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Attenzione");
                    alert.setHeaderText(null);
                    alert.setContentText("Inserisci le date di validità della scheda");
                    alert.showAndWait();
                    return;
                }

                Task<Void> taskCreazione = DBConnection.getInstance().insertSchedaClient(selectedClient.getId(), dataInizioPicker.getValue(), dataFinePicker.getValue(), obiettivi.getText(), noteGenerali.getText(), suggerimentiAlimentari.getText());

                Thread thread = new Thread(taskCreazione);
                thread.setDaemon(true);
                thread.start();

                taskCreazione.setOnSucceeded(suc->{
                    System.out.println(taskCreazione.getValue());
                    System.out.println("Scheda creata con successo");
                    vboxCenter.getChildren().remove(info);
                    loadSchedaCliente(selectedClient);
                });

                taskCreazione.setOnFailed(fal->{
                    System.err.println("Errore durante la creazione della scheda");
                    System.out.println(taskCreazione.getException());
                });

            });
            info.getChildren().addAll(ob, obiettivi, ng, noteGenerali, SuggAl, suggerimentiAlimentari, crea);

        });
    }


    private void loadEserciziScheda(int clientId, String selectedDay, TableView<Esercizio> esercizioTableView) {
        Task<ObservableList<Esercizio> > task = DBConnection.getInstance().getEserciziGiorno(clientId, selectedDay);

        task.setOnSucceeded(event -> {
            ObservableList<Esercizio> esercizi = task.getValue();

            esercizioTableView.setItems(esercizi);

        });

        task.setOnFailed(event -> {
            System.out.println(STR."Errore durante il caricamento delle informazioni: \{task.getException().getMessage()}");
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


    // TODO: logica per inserire i nuovi esercizi al database, devo collegare gli esercizi delle scheda con i dettagli della tab Esercizi
    @FXML
    private void aggiungiEsercizio() {
        EsercizioScheda nuovoEsercizio = new EsercizioScheda(serieSpinner.getValue(), ripetizioniSpinner.getValue(), recuperoSpinner.getValue(), noteEsercizioTextArea.getText(), esercizioTextField.getText(), gruppoMuscTextfield.getText(), giornoField.getText());

        if (nuovoEsercizio.nomeEserc() == null || nuovoEsercizio.nomeEserc().isEmpty()) {
            mostraErrore("Devi selezionare o inserire un esercizio!");
            return;
        }

        Task<Void> task = DBConnection.getInstance().insertEsercizioScheda(nuovoEsercizio, selectedClient.getId());



        /*

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
        esercizioTextField.setText(null);
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

    @FXML
    public void onNavigationButtonClick(ActionEvent event) {
        Button button = (Button) event.getSource();
        try {
            switch (button.getId()) {
                case "dashboardTrainer":
                    SceneHandlerPT.getInstance().setDashboardView();
                    break;
                case "attivitaPT":
                    SceneHandlerPT.getInstance().setAttivitaPTView();
                    break;
                case "creazioneScheda":
                    SceneHandlerPT.getInstance().setCreazioneSchedaView();
                    break;
                /*case "accountPT":
                    SceneHandlerPT.getInstance().setCreazioneSchedaView();
                    break;
                case "abbonamentoClient":
                    SceneHandlerPT.getInstance().setAbbonamentoView();
                    break;*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




