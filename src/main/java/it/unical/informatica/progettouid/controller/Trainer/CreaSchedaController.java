package it.unical.informatica.progettouid.controller.Trainer;

import it.unical.informatica.progettouid.model.*;
import it.unical.informatica.progettouid.view.AlertManager;
import it.unical.informatica.progettouid.view.SceneHandlerPT;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreaSchedaController {
    @FXML private ComboBox<String> clienteComboBox;
    @FXML private VBox schedaInfoPanel;
    @FXML private ScrollPane aggiungiEsercizioPanel;
    @FXML private TextField esercizioTextField;
    @FXML private TextField giornoField;
    @FXML private TextField serieField;
    @FXML private TextField ripetizioniField;
    @FXML private TextField recuperoField;
    @FXML private TextArea noteEsercizioTextArea;
    @FXML private TextArea obiettiviTextArea;
    @FXML private TextArea noteGeneraliTextArea;
    @FXML private TextArea suggerimentiAlimentariTextArea;
    @FXML private DatePicker dataInizioPicker;
    @FXML private DatePicker dataFinePicker;
    @FXML private TextField gruppoMuscTextfield;
    @FXML private FlowPane flowpaneScheda;
    @FXML private VBox info;
    private Map<String, Client> clientMap = new HashMap<>();
    private Client selectedClient = null;
    private int idScheda;


    @FXML
    private void initialize() {
        mostraClienti();
        clienteComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Client client = clientMap.get(newSelection);
                if (client != null) {
                    selectedClient = client;
                    dataInizioPicker.setValue(null);
                    dataFinePicker.setValue(null);
                    verificaSchedaClient();
                }
            }
        });

        dataInizioPicker.setEditable(false);
        dataFinePicker.setEditable(false);

    }

    private void loadInfoSchedaClient() {
        Task<SchedaAllenamento> task = DBConnection.getInstance().getInfoSchedaClient(selectedClient.id());
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(event -> {
            SchedaAllenamento infoScheda = task.getValue();
            if (infoScheda != null) {
                idScheda = infoScheda.idScheda();
                displaySchedaClient(infoScheda);
            }

        });

        task.setOnFailed(event -> {
            task.getException().printStackTrace();
        });

        new Thread(task).start();

    }

    private void modificaDataPicker(String dataInizio, String dataFine){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate dataI = LocalDate.parse(dataInizio, formatter);
            dataInizioPicker.setValue(dataI);
            LocalDate dataF = LocalDate.parse(dataFine, formatter);
            dataFinePicker.setValue(dataF);
        } catch (Exception e) {
            System.out.println(STR."Errore nella modifica dei datapicker: \{e.getMessage()}");
        }

    }

    private void displaySchedaClient(SchedaAllenamento infoScheda) {
        info.getChildren().clear();
        modificaDataPicker(infoScheda.dataInizio(), infoScheda.dataFine());

        HBox buttons = new HBox(10);
        Button modifica = new Button("Modifica scheda");
        modifica.setGraphic(new FontIcon("fas-edit"));
        Button aggEsercizio = new Button("Aggiungi esercizio");
        aggEsercizio.setGraphic(new FontIcon("fas-plus-square"));
        Button creaNuovaScheda = new Button("Crea nuova scheda");
        creaNuovaScheda.setGraphic(new FontIcon("fas-file"));
        Button salva = new Button("Salva modifiche");
        salva.setGraphic(new FontIcon("fas-save"));
        salva.setVisible(false);
        buttons.getChildren().addAll(modifica, aggEsercizio, creaNuovaScheda, salva);
        buttons.setAlignment(Pos.CENTER);
        modifica.setOnAction(event -> {
            salva.setVisible(true);
            obiettiviTextArea.setEditable(true);
            noteGeneraliTextArea.setEditable(true);
            suggerimentiAlimentariTextArea.setEditable(true);

            salva.setOnAction(e->{
                String ob = obiettiviTextArea.getText();
                String notes = noteGeneraliTextArea.getText();
                String sugg = suggerimentiAlimentariTextArea.getText();

                Task<Boolean> salvaModifiche = DBConnection.getInstance().insertModificheScheda(selectedClient.id(), ob, notes, sugg);
                Thread thread = new Thread(salvaModifiche);
                thread.setDaemon(true);
                thread.start();
                salvaModifiche.setOnSucceeded(ev->{
                    AlertManager al = new AlertManager(Alert.AlertType.CONFIRMATION, "Successo", null, "Modifiche salvate con successo");
                    al.display();
                    salva.setVisible(false);
                    obiettiviTextArea.setEditable(true);
                    noteGeneraliTextArea.setEditable(true);
                    suggerimentiAlimentariTextArea.setEditable(true);
                });
                salvaModifiche.setOnFailed(ev->{
                    System.out.println(STR."Errore durante il salvataggio: \{salvaModifiche.getException()}");
                });
            });
        });
        aggEsercizio.setOnAction(ev-> showFormModificaScheda());
        creaNuovaScheda.setOnAction(event->{
            creaSchedaClient();
        });

        info.getChildren().add(buttons);

        TabPane weekdayTabs = new TabPane();
        weekdayTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        VBox.setVgrow(weekdayTabs, Priority.ALWAYS);

        String[] giorniSet = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};

        for (String day : giorniSet) {
            Tab tab = new Tab(day);
            VBox vbox = new VBox(10);
            vbox.setPadding(new Insets(10));
            VBox.setVgrow(vbox, Priority.ALWAYS);

            TableView<Esercizio> tableView = createEsercizioTableView();
            VBox.setVgrow(tableView, Priority.ALWAYS);

            vbox.getChildren().add(tableView);
            tab.setContent(vbox);
            weekdayTabs.getTabs().add(tab);
        }

        weekdayTabs.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null && clienteComboBox.getSelectionModel().getSelectedItem() != null) {
                String selectedDay = newTab.getText();
                loadEserciziScheda(selectedDay, (TableView<Esercizio>) ((VBox) newTab.getContent()).getChildren().get(0));
            }
        });

        if (!weekdayTabs.getTabs().isEmpty()) {
            weekdayTabs.getSelectionModel().selectFirst();
            String selectedDay = weekdayTabs.getTabs().get(0).getText();
            loadEserciziScheda(selectedDay, (TableView<Esercizio>) ((VBox) weekdayTabs.getTabs().get(0).getContent()).getChildren().get(0));
        }

        info.getChildren().addAll(weekdayTabs);
    }

    private void showFormModificaScheda() {
        schedaInfoPanel.setVisible(false);
        aggiungiEsercizioPanel.setVisible(true);
    }

    private TableView<Esercizio> createEsercizioTableView() {
        TableView<Esercizio> tableView = new TableView<>();

        // Imposta la TableView per occupare tutto lo spazio disponibile
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // Definizione colonne con larghezze relative
        TableColumn<Esercizio, String> nomeEsercizioColumn = new TableColumn<>("Esercizio");
        TableColumn<Esercizio, Number> serieColumn = new TableColumn<>("Serie");
        TableColumn<Esercizio, Number> ripetizioniColumn = new TableColumn<>("Ripetizioni");
        TableColumn<Esercizio, Number> tempoRecuperoColumn = new TableColumn<>("Recupero");
        TableColumn<Esercizio, String> gMuscolareColumn = new TableColumn<>("Gruppo Muscolare");
        TableColumn<Esercizio, String> noteColumn = new TableColumn<>("Note");

        // Imposta le percentuali di larghezza delle colonne
        nomeEsercizioColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        serieColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.08));
        ripetizioniColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.10));
        tempoRecuperoColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.10));
        gMuscolareColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        noteColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));

        // Imposta i cell value factories
        nomeEsercizioColumn.setCellValueFactory(cellData -> cellData.getValue().nomeEsercProperty());
        serieColumn.setCellValueFactory(cellData -> cellData.getValue().nSerieProperty());
        ripetizioniColumn.setCellValueFactory(cellData -> cellData.getValue().nRipetizioniProperty());
        tempoRecuperoColumn.setCellValueFactory(cellData -> cellData.getValue().tmpRecuperoProperty());
        gMuscolareColumn.setCellValueFactory(cellData -> cellData.getValue().gMuscolareProperty());
        noteColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());

        // Abilita il wrap del testo per le colonne con testo lungo
        noteColumn.setCellFactory(tc -> {
            TableCell<Esercizio, String> cell = new TableCell<>() {
                private Text text = new Text();
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        text.setText(item);
                        text.setWrappingWidth(tc.getWidth() - 10);
                        setGraphic(text);
                    }
                }
            };
            return cell;
        });

        // Aggiungi le colonne alla TableView
        tableView.getColumns().addAll(
                nomeEsercizioColumn, serieColumn, ripetizioniColumn,
                tempoRecuperoColumn, gMuscolareColumn,
                noteColumn
        );

        return tableView;
    }

    private void verificaSchedaClient() {
        Task<Boolean> task = DBConnection.getInstance().clientHaUnaScheda(selectedClient.id());
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(e -> {
            Boolean result = task.getValue();
            System.out.println(result);
            if(result){
                loadInfoSchedaClient();
            }
            else{
                creaSchedaClient();
            }
        });

        task.setOnFailed(e -> {
            System.out.println(task.getException());
        });

    }

    private void creaSchedaClient() {
        info.getChildren().clear();
        Label nota = new Label(STR."\{selectedClient.nome()} non ha ancora una scheda, creala subito");
        Button creaScheda = new Button("Crea scheda");
        info.getChildren().addAll(nota, creaScheda);

        creaScheda.setOnAction(event-> {
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            VBox cont = new VBox(10);
            Label ob = new Label("Obiettivi");
            TextField obiettivi = new TextField();
            Label ng = new Label("Note generali:");
            TextField noteGenerali = new TextField();
            Label SuggAl = new Label("Suggerimenti alimentari");
            TextField suggerimentiAlimentari = new TextField();
            Button crea = new Button("Crea");
            crea.setOnAction(ev -> {
                dataInizioPicker.setEditable(true);
                dataFinePicker.setEditable(true);
                if(dataFinePicker.getValue() == null || dataInizioPicker.getValue() == null){
                    AlertManager info = new AlertManager(Alert.AlertType.INFORMATION, "Attenzione", null, "Inserisci la data di inizio e di fine validità");
                    info.display();
                } else {
                    Task<Void> taskCreazione = DBConnection.getInstance().insertSchedaClient(selectedClient.id(), dataInizioPicker.getValue(), dataFinePicker.getValue(), obiettivi.getText(), noteGenerali.getText(), suggerimentiAlimentari.getText());
                    Thread thread = new Thread(taskCreazione);
                    thread.setDaemon(true);
                    thread.start();

                    taskCreazione.setOnSucceeded(suc -> {
                        System.out.println(taskCreazione.getValue());
                        System.out.println("Scheda creata con successo");
                        info.getChildren().clear();
                        loadInfoSchedaClient();


                    });

                    taskCreazione.setOnFailed(fal -> {
                        System.err.println("Errore durante la creazione della scheda");
                        System.out.println(taskCreazione.getException());
                    });
                }
            });

            creaScheda.setDisable(true);
            cont.getChildren().addAll(ob, obiettivi, ng, noteGenerali, SuggAl, suggerimentiAlimentari, crea);
            scrollPane.setContent(cont);
            info.getChildren().add(scrollPane);
        });
    }

    private void loadEserciziScheda(String selectedDay, TableView<Esercizio> esercizioTableView) {
        Task<ObservableList<Esercizio> > task = DBConnection.getInstance().getEserciziGiorno(selectedClient.id(), selectedDay);

        task.setOnSucceeded(event -> {
            ObservableList<Esercizio> esercizi = task.getValue();

            // debug
            if (esercizi == null || esercizi.isEmpty()) {
                System.out.println("Nessun esercizio trovato per il giorno: " + selectedDay);
            } else {
                System.out.println("Esercizi caricati per il giorno: " + selectedDay);
                esercizi.forEach(e -> System.out.println(e));
            }

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
                AlertManager errore = new AlertManager(Alert.AlertType.ERROR, "Errore", null, "Errore nel caricamento dei client");
            } else {
                for (Client client : clients) {
                    String clientName = STR."\{client.nome()} \{client.cognome()}";
                    clienteComboBox.getItems().add(clientName);
                    clientMap.put(clientName, client);
                }
            }
        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento dei personal(listVirw): " + task.getException().getMessage());
        });

        new Thread(task).start();
    }


    @FXML
    private void aggiungiEsercizio() {
        EsercizioScheda nuovoEsercizio = new EsercizioScheda(serieField.getText(), ripetizioniField.getText(), recuperoField.getText(), noteEsercizioTextArea.getText(), esercizioTextField.getText(), gruppoMuscTextfield.getText(), giornoField.getText());

        if (nuovoEsercizio.nomeEserc() == null || nuovoEsercizio.nomeEserc().isEmpty()) {
            AlertManager warn = new AlertManager(Alert.AlertType.INFORMATION, "Attenzione", null, "Per continuare devi selezionare un esercizio");
            return;
        }

        Task<Void> task = DBConnection.getInstance().insertEsercizioScheda(nuovoEsercizio, selectedClient.id());

        task.setOnSucceeded(e ->{
            AlertManager al = new AlertManager(Alert.AlertType.CONFIRMATION, "Esercizio aggiunto", null, "Esercizio aggiunto");
            al.display();
            pulisciFormEsercizio();
        }
        );


        mostraPannelloPrincipale();
    }


    @FXML
    private void anteprimaPDF() {
        AlertManager al = new AlertManager(Alert.AlertType.INFORMATION, "Funzionalità in arrivo", null, "Presto sarà possibile visualizzare la scheda in formato pdf");
        al.display();
    }


    public void annullaAggiuntaEsercizio(ActionEvent actionEvent) {
        pulisciFormEsercizio();
        mostraPannelloPrincipale();
    }

    private void pulisciFormEsercizio() {
        esercizioTextField.setText(null);
        serieField.setText(null);
        ripetizioniField.setText(null);
        recuperoField.setText(null);
        noteEsercizioTextArea.clear();
    }

    private void mostraPannelloPrincipale() {
        schedaInfoPanel.setVisible(true);
        aggiungiEsercizioPanel.setVisible(false);
    }

    @FXML
    public void onNavigationButtonClick(ActionEvent event) {
        Button button = (Button) event.getSource();
        try {
            switch (button.getId()) {
                case "dashboardPT":
                    SceneHandlerPT.getInstance().setDashboardView();
                    break;
                case "articoli":
                    SceneHandlerPT.getInstance().setArticoliView();
                    break;
                case "creazioneScheda":
                    SceneHandlerPT.getInstance().setCreazioneSchedaView();
                    break;
                case "impostazioniPT":
                    SceneHandlerPT.getInstance().setImpostazioniView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




