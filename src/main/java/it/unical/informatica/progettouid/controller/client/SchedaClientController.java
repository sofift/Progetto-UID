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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SchedaClientController {
    @FXML private VBox ptInfoContainer;
    @FXML private VBox vboxCenter;
    private int idScheda;
    private Map<String, Integer> ptMap = new HashMap<>();


    @FXML
    public void initialize() {
        verificaSchedaClient();
        loadSchedaInfo();
        loadPTInfo();
    }

    private void verificaSchedaClient() {
        int idClient = ClientSession.getInstance().getCurrentClient().id();
        Task<Boolean> task = DBConnection.getInstance().clientHaUnaScheda(idClient);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(e -> {
            Boolean result = task.getValue();
            System.out.println(result);
            if(result){
                loadSchedaInfo();
            }
            else{
                displayRichiestaScheda();
            }
        });

        task.setOnFailed(e -> {
            System.out.println(task.getException());
        });

    }

    private void loadSchedaInfo() {
        int idClient = ClientSession.getInstance().getCurrentClient().id();
        Task<SchedaAllenamento> task = DBConnection.getInstance().getInfoSchedaClient(idClient);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        task.setOnSucceeded(event -> {
            SchedaAllenamento info = task.getValue();
            if (info != null) {
                idScheda = info.idScheda();
                displaySchedaInfo(info);
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
            ComboBox<String> diplayPt = new ComboBox<>();
            mostraPT(diplayPt);
            Label ob = new Label("Qual è il tuo obiettivo?");
            TextField obiettivo = new TextField();
            Label note = new Label("Inserisci cosa deve sapere il tuo personal trainer");
            TextField notes = new TextField();
            Button invia = new Button("Invia");
            invia.setOnAction(e ->{
                invia.setDisable(true);
                String selectedPt = diplayPt.getValue();
                if (selectedPt == null || !ptMap.containsKey(selectedPt)) {
                    AlertManager errorAlert = new AlertManager(Alert.AlertType.ERROR, "Errore", null, "Seleziona un personal trainer valido.");
                    errorAlert.display();
                    invia.setDisable(false);
                    return;
                }
                int selectedId = ptMap.get(selectedPt);

                Task<Void> task = DBConnection.getInstance().insertNotifyTrainer(selectedId, STR."Il/La signore/a \{ClientSession.getInstance().getCurrentClient().nome()} ha richiesto una scheda di allenamento");
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

    private void mostraPT(ComboBox<String> diplayPt) {
       Task<List<PersonalTrainer>> task = DBConnection.getInstance().getAllPt();

       task.setOnSucceeded(event -> {
           diplayPt.getItems().clear();
           ptMap.clear();
           List<PersonalTrainer> pt = task.getValue();
           if (pt.isEmpty()) {
               AlertManager alert = new AlertManager(Alert.AlertType.ERROR,"Errore" ,null, "Erorre nel caricamento dei client");
               alert.display();
           } else {
               for (PersonalTrainer PT : pt) {
                   String nomecognome = STR."\{PT.nome()} \{PT.cognome()}";
                   ptMap.put(nomecognome, PT.id());
                   diplayPt.getItems().add(nomecognome);
               }
           }

           diplayPt.setOnAction(e -> {
               String selectedPt = diplayPt.getValue();
               if (selectedPt != null && ptMap.containsKey(selectedPt)) {
                   int selectedId = ptMap.get(selectedPt);

               }
           });
       });

       task.setOnFailed(ev -> {
                System.out.println(STR."Errore durante il caricamento dei personal(listVirw): \{task.getException().getMessage()}");
       });
       new Thread(task).start();
    }


    private void displaySchedaInfo(SchedaAllenamento info) {
        System.out.println(info); // Debug
        vboxCenter.getChildren().clear();
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

        if (!weekdayTabs.getTabs().isEmpty()) {
            weekdayTabs.getSelectionModel().selectFirst();
            String selectedDay = weekdayTabs.getTabs().get(0).getText();
            loadEserciziScheda(ClientSession.getInstance().getCurrentClient().id(), selectedDay, (TableView<Esercizio>) ((VBox) weekdayTabs.getTabs().get(0).getContent()).getChildren().get(0));
        }

        Label notERac = new Label("Note e Raccomandazioni");
        VBox cont = new VBox();
        Label note = new Label("Note");
        Text notes = new Text(info.notes());
        Label sugAl = new Label("Raccomandazioni");
        Text sugAlimentari = new Text(info.suggerimentiAlimentari());
        /*TextArea notes = new TextArea();
        TextArea sugAlimentari = new TextArea(info.suggerimentiAlimentari());
        notes.setEditable(false);
        sugAlimentari.setEditable(false);
        notes.setWrapText(true);
        sugAlimentari.setWrapText(true);*/
        cont.getChildren().addAll(note, notes, sugAl, sugAlimentari);
        vboxCenter.getChildren().addAll(weekdayTabs, notERac, cont);
    }

    private TableView<Esercizio> createEsercizioTableView() {
        TableView<Esercizio> tableView = new TableView<>();

        // Imposta la TableView per occupare tutto lo spazio disponibile
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        TableColumn<Esercizio, String> nomeEsercizioColumn = new TableColumn<>("Esercizio");
        TableColumn<Esercizio, Number> serieColumn = new TableColumn<>("Serie");
        TableColumn<Esercizio, Number> ripetizioniColumn = new TableColumn<>("Ripetizioni");
        TableColumn<Esercizio, Number> tempoRecuperoColumn = new TableColumn<>("Recupero");
        TableColumn<Esercizio, String> gMuscolareColumn = new TableColumn<>("Gruppo Muscolare");
        TableColumn<Esercizio, String> noteColumn = new TableColumn<>("Note");

        nomeEsercizioColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        serieColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.08));
        ripetizioniColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.10));
        tempoRecuperoColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.10));
        gMuscolareColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        noteColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));

        nomeEsercizioColumn.setCellValueFactory(cellData -> cellData.getValue().nomeEsercProperty());
        serieColumn.setCellValueFactory(cellData -> cellData.getValue().nSerieProperty());
        ripetizioniColumn.setCellValueFactory(cellData -> cellData.getValue().nRipetizioniProperty());
        tempoRecuperoColumn.setCellValueFactory(cellData -> cellData.getValue().tmpRecuperoProperty());
        gMuscolareColumn.setCellValueFactory(cellData -> cellData.getValue().gMuscolareProperty());
        noteColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());

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
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(event -> {
            ObservableList<Esercizio> esercizi = task.getValue();
            //debug
            if (esercizi == null || esercizi.isEmpty()) {
                System.out.println("Nessun esercizio trovato per il giorno: " + selectedDay);
            } else {
                System.out.println("Esercizi caricati per il giorno: " + selectedDay);
                esercizi.forEach(e -> System.out.println(e));
            }
            esercizioTableView.setItems(esercizi);

        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento delle informazioni: " + task.getException().getMessage());
        });

    }

    @FXML
    private void stampaScheda(){
        AlertManager al = new AlertManager(Alert.AlertType.INFORMATION, "Funzionalità in arrivo", null, "Presto sarà possibile visualizzare la scheda in formato pdf");
        al.display();
    }

    @FXML
    private void condividiScheda(){
        AlertManager al = new AlertManager(Alert.AlertType.INFORMATION, "Funzionalità in arrivo", null, "Presto sarà possibile visualizzare la scheda in formato pdf");
        al.display();
    }

    private void contactPT(int id) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Contatta PT");
        dialog.setHeaderText(null);
        dialog.setContentText("Invio messaggio al tuo PT...");
        dialog.showAndWait().ifPresent(response -> {
            if (!response.isEmpty()) {
                String nomeCognomeClient = ClientSession.getInstance().getCurrentClient().nome() + ClientSession.getInstance().getCurrentClient().cognome();
                String message = "Richiesta: " + response + "dal cliente " + nomeCognomeClient;
                Task<Void> task = DBConnection.getInstance().insertNotifyTrainer(id, message);
                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
                task.setOnSucceeded(event->{
                    AlertManager conferma = new AlertManager(Alert.AlertType.CONFIRMATION, "Conferma", null, "Il tuo messagio è stato appena inviato");
                    conferma.display();
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