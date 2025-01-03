package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.model.*;
import it.unical.informatica.progettouid.view.AlertManager;
import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AbbonamentoClientController {
    @FXML public VBox vBoxPianiOfferti;
    @FXML public Label descrizioneLabel;
    @FXML public VBox datiAbbonamentoVBox;
    @FXML public VBox centerBox;
    @FXML public ScrollPane scrollPiani;

    @FXML
    public void initialize() {
        loadInformazioniCliente();
        loadPianiOfferti();
    }

    private void loadInformazioniCliente() {
        Task<InfoBaseAbbonamento> task = DBConnection.getInstance().getInfoAbbonamento();
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();


        task.setOnSucceeded(event -> {
            InfoBaseAbbonamento info = task.getValue();
            if(info == null) {
                scrollPiani.setPrefWidth(500);
                Label clientNonAbbonato = new Label("Ops, pare che tu non abbia ancora un abbonamento, scorri tra i nostri piani disponibili e scelgi quello più adatto a te");
                datiAbbonamentoVBox.getChildren().add(clientNonAbbonato);
                return;
            }
            else{
                displayInfoAbbonamento(info);
            }

        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento delle informazioni: " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    private void displayInfoAbbonamento(InfoBaseAbbonamento info) {
        HBox abbonamento = new HBox();
        FontIcon tipoAbbonamentoIcon = new FontIcon();
        Label tipoAbbonamentoLabel = new Label(info.nomeAbbonamento());
        abbonamento.getChildren().addAll(tipoAbbonamentoIcon, tipoAbbonamentoLabel);

        VBox infoAbbonamento = new VBox();
        Label dateInit = new Label("Data inizio:");
        Label dataInizioLabel = new Label(info.dataInizio());
        Label dataScad = new Label("Data scadenza:");
        Label dataScadenzaLabel = new Label(info.dataScadenza());
        Label stato = new Label("Stato:");
        Label statoAbbonamentoLabel = new Label(info.stato());

        infoAbbonamento.getChildren().addAll(dataInizioLabel, dataScadenzaLabel, statoAbbonamentoLabel);

        Button rinnovo = new Button("Rinnova il tuo piano");
        datiAbbonamentoVBox.getChildren().addAll(abbonamento, infoAbbonamento, rinnovo);
    }

    private void loadPianiOfferti() {
        Task<List<TipiAbbonamento>> task = DBConnection.getInstance().getAllPianiAbbonamento();
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(event -> {
            List<TipiAbbonamento> piani = task.getValue();
            Platform.runLater(() -> {
                displayPiani(piani);
            });


        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento dei piani(listVirw): " + task.getException().getMessage());
        });

        new Thread(task).start();

    }

    private void displayPiani(List<TipiAbbonamento> piani) {
        vBoxPianiOfferti.getChildren().clear();
        Label pianiDisp = new Label("Piani disponibili");
        vBoxPianiOfferti.getChildren().addAll(pianiDisp);
        for(TipiAbbonamento p : piani) {
            VBox card = new VBox();
            Label nome = new Label(p.nome());
            Label destinatoA = new Label(p.dedicatoA());
            Label prezzo = new Label("" + p.prezzo());

            Text descrizione = new Text(p.descrizione());
            Button seleziona = new Button("Seleziona");
            seleziona.setOnAction(e -> showFormAbbonamento(p));

            card.getChildren().addAll(nome, destinatoA, prezzo, descrizione, seleziona);
            vBoxPianiOfferti.getChildren().add(card);
        }


    }

    private void showFormAbbonamento(TipiAbbonamento abbonamento) {
        //scrollPiani.setPrefWidth(300);
        centerBox.getChildren().clear();
        VBox formBox = new VBox(10);

        Label title = new Label("Acquisto abbonamento");
        Client loggedClient = ClientSession.getInstance().getCurrentClient();

        Label nomeLabel = new Label(STR."Nome: \{loggedClient.getNome()}");
        nomeLabel.setDisable(true);

        Label cognomeLabel = new Label(STR."Cognome: \{loggedClient.getCognome()}");
        cognomeLabel.setDisable(true);

        Label numeroCartaLabel = new Label("Numero Carta:");
        TextField numeroCartaField = new TextField();
        numeroCartaField.setPromptText("0000 0000 000000");

        Label scadenzaLabel = new Label("Scadenza:");
        TextField scadenzaField = new TextField();
        scadenzaField.setPromptText("mm/aaaa");

        Label cvvLabel = new Label("CVV:");
        TextField cvvField = new TextField();
        cvvField.setPromptText("123");

        Separator separator = new Separator();

        Label resoConto = new Label("Piano: ");
        Label nome = new Label(abbonamento.nome());
        Label prezzo = new Label(STR."\{abbonamento.prezzo()}€");

        formBox.getChildren().addAll(title, nomeLabel, cognomeLabel, numeroCartaLabel, numeroCartaField, scadenzaLabel, scadenzaField, cvvLabel, cvvField);
        formBox.getChildren().addAll(separator, resoConto, nome, prezzo);

        HBox hboxButton = new HBox();
        hboxButton.setSpacing(20);
        Button confermaButton = new Button("Conferma pagamento");
        Button annulla = new Button("Annulla");
        annulla.setOnAction(e->loadInformazioniCliente());
        hboxButton.getChildren().addAll(confermaButton, annulla);
        confermaButton.setOnAction(e -> processaPagamento(numeroCartaField.getText(), scadenzaField.getText(), cvvField.getText(), abbonamento.id(), abbonamento.prezzo()));

        formBox.getChildren().add(hboxButton);


        centerBox.getChildren().add(formBox);
    }

    private void processaPagamento(String numeroCarta, String scadenzaLabel, String cvv, int idAbbonamento, int importo) {
        numeroCarta = numeroCarta.replaceAll("\\s+", "");
        if (!numeroCarta.matches("\\d{13,19}")) {
            AlertManager al = new AlertManager(Alert.AlertType.ERROR, "Attenzione", null, "Il numero di carta non è valido");
            al.showAndWait();
            registraPagamento(idAbbonamento, importo, "fallito");
        } else if (!cvv.matches("\\d{3}")) {
            AlertManager al = new AlertManager(Alert.AlertType.ERROR, "Attenzione", null, "Il CVV non è valido");
            al.showAndWait();
            registraPagamento(idAbbonamento, importo, "fallito");
        } else if (!isDateValid(scadenzaLabel)) {
            AlertManager al = new AlertManager(Alert.AlertType.ERROR, "Attenzione", null, "La data non è valida");
            al.showAndWait();
            registraPagamento(idAbbonamento, importo, "fallito");
        } else {
            registraPagamento(idAbbonamento, importo, "avvenuto");
        }
    }

    //
    // TODO: rivedere la finestra di dialogo perchè mi mostra entrmbi gli avvisi di pagamento
    private void registraPagamento(int idAbbonamento, int importo, String stato) {
        Task<Void> task = DBConnection.getInstance().insertPagamento(idAbbonamento, importo, stato);
        Thread thread = new Thread(task);
        thread.start();

        task.setOnSucceeded(e->{
            if(stato.equals("avvenuto")){
                AlertManager conferma  = new AlertManager(Alert.AlertType.CONFIRMATION, "Pagamento avvenuto con successo", null, "Pagamento andato a buon fine");
                conferma.display();
            }
            else if(stato.equals("fallito")){
                AlertManager fal  = new AlertManager(Alert.AlertType.ERROR, "Pagamento fallito", null, "Pagamento non andato a buonfine");
                fal.display();
            }
        });

        task.setOnFailed(e->{
            task.getException().printStackTrace();
        });
    }

    private static boolean isDateValid(String dataScadenza) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        try {
            // Converte la stringa di scadenza in un oggetto YearMonth
            YearMonth expirationDate = YearMonth.parse(dataScadenza, formatter);
            // Ottiene il YearMonth corrente
            YearMonth currentMonth = YearMonth.now();
            // Controlla se la data di scadenza è prima del mese corrente
            return !expirationDate.isBefore(currentMonth);
        } catch (DateTimeParseException e) {
            return false;  // Errore nel parsing della data
        }
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
