package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.*;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
                Task<Void> task = DBConnection.getInstance().insertNotifyTrainer(diplayPt.getValue().getId(), STR."Il/La signore/a \{ClientSession.getInstance().getCurrentClient().getNome()} ha richiesto una scheda di allenamento");
                Thread thread = new Thread(task);
                thread.start();

                task.setOnSucceeded(event1->{
                    Alert confirmDialog = new Alert(Alert.AlertType.INFORMATION);
                    confirmDialog.setTitle("Richiesta inviata!");
                    confirmDialog.setContentText("SchedaRichiesta");
                    confirmDialog.setHeaderText(null);
                    confirmDialog.showAndWait();
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
               showAlertSucc("Errore", "Erorre nel caricamento dei client");
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
        Label stato = new Label(STR."Stato: \{info.statoScheda()}");

        vboxCenter.getChildren().addAll(infoGen, dataInizio, dataFine, obiettivo, stato);

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
            if (newTab != null) {
                String selectedDay = newTab.getText();
                loadEserciziScheda(ClientSession.getInstance().getCurrentClient().getId(), selectedDay, (TableView<Esercizio>) ((VBox) newTab.getContent()).getChildren().get(0));
            }
        });

        Label notERac = new Label("Note e Raccomandazioni");
        HBox cont = new HBox();
        TextArea notes = new TextArea(info.notes());
        TextArea sugAlimentari = new TextArea(info.suggerimentiAlimentari());
        cont.getChildren().addAll(notes, sugAlimentari);
        vboxCenter.getChildren().addAll(notERac, cont);
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

    private void loadPTInfo() {
        Task<PersonalTrainer> task = DBConnection.getInstance().getInfoPT();

        task.setOnSucceeded(event -> {
            PersonalTrainer info = task.getValue();

            displayPTInfo(info);

        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento delle informazioni: " + task.getException().getMessage());
        });

        new Thread(task).start();


    }

    private void displayPTInfo(PersonalTrainer info) {
        if(info != null) {
            VBox ptCard = new VBox(10);
            //ptCard.getStyleClass().add("pt-card");

            Label nameLabel = new Label(info.getNome());
            //nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label specializzazioneLabel = new Label(info.getSpecializzazione());
            Label emailLabel = new Label(info.getEmail());
            Label phoneLabel = new Label(info.getTelefono());

            Button contactButton = new Button("Contatta PT");
            contactButton.setMaxWidth(Double.MAX_VALUE);
            contactButton.setOnAction(e -> contactPT());

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


    //TODO: implementare logica personal trainer
    private void contactPT() {
        // Implementa la logica per contattare il PT
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Contatta PT");
        alert.setHeaderText(null);
        alert.setContentText("Invio messaggio al Personal Trainer...");
        alert.showAndWait();
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlertSucc(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}