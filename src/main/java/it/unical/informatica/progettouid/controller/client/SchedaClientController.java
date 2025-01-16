package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.*;
import it.unical.informatica.progettouid.view.AlertManager;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.List;

// TODO: cambiare il compobox con i nomi dei personal, stessa cosa dei client nel trainer
public class SchedaClientController {
    @FXML public TextArea sugAlimentari;
    @FXML private GridPane infoGrid;
    @FXML private TabPane weekdayTabs;
    @FXML private TextArea notesArea;
    @FXML private VBox ptInfoContainer;
    @FXML private VBox statsContainer;

    // VBox per ogni giorno della settimana
    @FXML private VBox vboxCenter;

    private int idScheda;

    @FXML
    public void initialize() {
        loadSchedaInfo();

        loadPTInfo();
    }

    private void loadSchedaInfo() {
        Task<SchedaAllenamento> task = DBConnection.getInstance().getInfoSchedaClient();

        task.setOnSucceeded(event -> {
            SchedaAllenamento info = task.getValue();
            if (info != null) {
                idScheda = info.idScheda();
                displaySchedaInfo(info);
            } else {
                displayRichiestaScheda();
            }

        });

        task.setOnFailed(event -> {
            task.getException().printStackTrace();
        });

        new Thread(task).start();

    }

    private void displayRichiestaScheda() {
        Label richiediScheda = new Label("Pare che tu non abbia ancora una scheda di allenamento, cosa stai aspettando?! Richiedila subito");
        Button buttonRichiedi = new Button("Richiedi");
        buttonRichiedi.setOnAction((ActionEvent event) -> {
            buttonRichiedi.setDisable(true);
            VBox inserisciInfo = new VBox();
            Label pt = new Label("Con quale personal trainer vuoi richiedere la tua scheda?");
            ComboBox<PersonalTrainer> diplayPt = new ComboBox<>();
            mostraPT(diplayPt);
            Label ob = new Label("Qual è il tuo obiettivo?");
            TextField obiettivo = new TextField();
            Label note = new Label("Inserisci cosa deve sapere il tuo personal trainer");
            TextField notes = new TextField();
            Button invia = new Button("Invia");
            invia.setOnAction(e ->{
                invia.setDisable(true);
                Task<Void> task = DBConnection.getInstance().insertNotifyTrainer(diplayPt.getValue().id(), STR."Il/La signore/a \{ClientSession.getInstance().getCurrentClient().nome()} ha richiesto una scheda di allenamento");
                Thread thread = new Thread(task);
                thread.start();

                task.setOnSucceeded(event1->{
                    AlertManager confirmDialog = new AlertManager(Alert.AlertType.CONFIRMATION, "Richiesta inviata", null, "Scheda richiesta");
                });

                task.setOnFailed(event1 ->{
                    task.getException().printStackTrace();
                });
            } );
            inserisciInfo.getChildren().addAll(pt, diplayPt, ob, obiettivo, note, notes, invia);
            vboxCenter.getChildren().add(inserisciInfo);
        });

        vboxCenter.getChildren().addAll(richiediScheda, buttonRichiedi);
    }

    private void mostraPT(ComboBox<PersonalTrainer> diplayPt) {
       Task<List<PersonalTrainer>> task = DBConnection.getInstance().getAllPt();

       task.setOnSucceeded(event -> {
           diplayPt.getItems().clear();
           List<PersonalTrainer> pt = task.getValue();
           if (pt.isEmpty()) {
               AlertManager alert = new AlertManager(Alert.AlertType.ERROR,"Errore" ,null, "Erorre nel caricamento dei client");
               alert.display();
           } else {
               for (PersonalTrainer PT : pt) {
                   diplayPt.getItems().add(PT);
               }
           }
       });
       task.setOnFailed(event -> {
                System.out.println("Errore durante il caricamento dei personal(listVirw): " + task.getException().getMessage());
            });
            new Thread(task).start();
    }


    private void displaySchedaInfo(SchedaAllenamento info) {
        Label infoGen = new Label("Informazioni generali");
        Label dataInizio = new Label(STR."Data inizio: \{info.dataInizio()}");
        Label dataFine = new Label(STR."Data fine: \{info.dataFine()}");
        Label obiettivo = new Label(STR."Obiettivi : \{info.obiettivi()}");

        vboxCenter.getChildren().addAll(infoGen, dataInizio, dataFine, obiettivo);


        TabPane weekdayTabs = new TabPane();
        weekdayTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        VBox.setVgrow(weekdayTabs, Priority.ALWAYS);
        String[] giorniSet = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};

        for (String day : giorniSet) {
            Tab tab = new Tab(day);
            VBox vbox = new VBox(10); // spacing di 10
            vbox.setPadding(new Insets(10));
            VBox.setVgrow(vbox, Priority.ALWAYS);

            TableView<Esercizio> tableView = createEsercizioTableView();
            VBox.setVgrow(tableView, Priority.ALWAYS);

            vbox.getChildren().add(tableView);
            tab.setContent(vbox);
            weekdayTabs.getTabs().add(tab);
        }

        weekdayTabs.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                String selectedDay = newTab.getText();
                loadEserciziScheda(ClientSession.getInstance().getCurrentClient().id(), selectedDay, (TableView<Esercizio>) ((VBox) newTab.getContent()).getChildren().get(0));
            }
        });

        Label notERac = new Label("Note e Raccomandazioni");
        HBox cont = new HBox();
        TextArea notes = new TextArea(info.notes());
        TextArea sugAlimentari = new TextArea(info.suggerimentiAlimentari());
        cont.getChildren().addAll(notes, sugAlimentari);
        vboxCenter.getChildren().addAll(weekdayTabs, notERac, cont);
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
        TableColumn<Esercizio, String> difficoltaColumn = new TableColumn<>("Difficoltà");
        TableColumn<Esercizio, String> noteColumn = new TableColumn<>("Note");
        TableColumn<Esercizio, String> descrizioneColumn = new TableColumn<>("Descrizione");

        // Imposta le percentuali di larghezza delle colonne
        nomeEsercizioColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        serieColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.08));
        ripetizioniColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.10));
        tempoRecuperoColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.10));
        gMuscolareColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        difficoltaColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.10));
        noteColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        descrizioneColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.17));

        // Imposta i cell value factories
        nomeEsercizioColumn.setCellValueFactory(cellData -> cellData.getValue().nomeEsercProperty());
        serieColumn.setCellValueFactory(cellData -> cellData.getValue().nSerieProperty());
        ripetizioniColumn.setCellValueFactory(cellData -> cellData.getValue().nRipetizioniProperty());
        tempoRecuperoColumn.setCellValueFactory(cellData -> cellData.getValue().tmpRecuperoProperty());
        gMuscolareColumn.setCellValueFactory(cellData -> cellData.getValue().gMuscolareProperty());
        difficoltaColumn.setCellValueFactory(cellData -> cellData.getValue().diffProperty());
        noteColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());
        descrizioneColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());

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
                tempoRecuperoColumn, gMuscolareColumn, difficoltaColumn,
                noteColumn, descrizioneColumn
        );

        return tableView;
    }

    private void loadPTInfo() {
        Task<PersonalTrainer> task = DBConnection.getInstance().getPersonalFromSchedaClient();

        task.setOnSucceeded(event -> {
            PersonalTrainer info = task.getValue();
            displayPTInfo(info);
        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento delle informazioni: " + task.getException().getMessage());
        });

        new Thread(task).start();


    }

    private void displayPTInfo(PersonalTrainer PTInfo) {
        if(PTInfo != null) {
            VBox ptCard = new VBox(10);
            //ptCard.getStyleClass().add("pt-card");

            Label nameLabel = new Label(PTInfo.nome());
            //nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label specializzazioneLabel = new Label(PTInfo.specializzazione());
            Label emailLabel = new Label(PTInfo.email());
            Label phoneLabel = new Label(PTInfo.telefono());

            Button contactButton = new Button("Contatta PT");
            contactButton.setMaxWidth(Double.MAX_VALUE);
            contactButton.setOnAction(e -> contactPT(PTInfo.id()));

            ptCard.getChildren().addAll(nameLabel, specializzazioneLabel, emailLabel, phoneLabel, contactButton);
            ptInfoContainer.getChildren().add(ptCard);
        }
        else{
            ptInfoContainer.getChildren().clear();
        }
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


    private void contactPT(int id) {
        // Implementa la logica per contattare il PT
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Contatta PT");
        dialog.setHeaderText(null);
        dialog.setContentText("Invio messaggio al tuo PT...");
        dialog.showAndWait().ifPresent(response -> {
            if (!response.isEmpty()) {
                Task<Void> task = DBConnection.getInstance().insertNotifyTrainer(id, response);

                task.setOnSucceeded(event->{
                    AlertManager conferma = new AlertManager(Alert.AlertType.CONFIRMATION, "Conferma", null, "Il tuo messagio è stato appena inviato");
                });

                task.setOnFailed(event -> {
                   task.getException().printStackTrace();
                });
            }
        });
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


}