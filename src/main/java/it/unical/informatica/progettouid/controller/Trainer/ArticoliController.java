package it.unical.informatica.progettouid.controller.Trainer;


import it.unical.informatica.progettouid.model.Articolo;
import it.unical.informatica.progettouid.model.DBConnection;
import it.unical.informatica.progettouid.view.SceneHandlerPT;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.util.List;


// TODO RIVEDERE MEGLIO LA LIST VIEW, NON MI CARICA GLI ARTICOLI
// TODO VEDERE COME CARICARE GLI ARTICOLIQUANDO IL PULSANTE TORNA INDEITRO

public class ArticoliController {
    @FXML private VBox vboxContent;
    @FXML private VBox vboxRight;

    private void displayArticoli(List<Articolo> articoli) {
        vboxContent.getChildren().clear();
        ListView<HBox> listArticoli = new ListView<>();
        listArticoli.setFixedCellSize(100);
        listArticoli.setPrefHeight(articoli.size() * listArticoli.getFixedCellSize());
        listArticoli.setCellFactory(lv -> new ListCell<HBox>() {
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    setPadding(new Insets(4, 0, 4, 0));
                }
            }
        });

        for(Articolo a : articoli){
            HBox content = new HBox(10);
            content.setAlignment(Pos.CENTER_LEFT);
            content.setPadding(new Insets(0, 16, 0, 16));
            VBox textContainer = new VBox(5);
            Label titolo = new Label(a.titolo());
            titolo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label descrizione = new Label(a.descrizione());
            descrizione.setStyle("-fx-font-size: 12px;");
            descrizione.setWrapText(true);

            textContainer.getChildren().addAll(titolo, descrizione);
            Button visualizza = new Button("Visualizza");
            visualizza.setOnAction(event -> displayArticoloSelezionato(a.id(), a.titolo(), a.URL(), articoli));

            content.getChildren().addAll(textContainer, visualizza);
            listArticoli.getItems().add(content);
        }
        vboxContent.getChildren().add(listArticoli);
        VBox.setVgrow(listArticoli, Priority.ALWAYS);

    }

    private void displayArticoloSelezionato(int id, String titolo, String URL, List<Articolo> articoli) {
        vboxContent.getChildren().clear();
        Button indietro = new Button("<-");
        indietro.setOnAction(e->{
            displayArticoli(articoli);
        });
        WebView web = new WebView();
        VBox.setVgrow(web, Priority.ALWAYS);
        web.getEngine().load(URL);

        Label titoloLabel = new Label(titolo);

        vboxRight.getChildren().addAll(titoloLabel, web);
    }

    public void loadContentPerCategoria(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource(); // Ottieni il bottone che ha scatenato l'evento
        String categoria = btn.getId(); // Utilizza l'ID del bottone come categoria

        Task<List<Articolo>> task = DBConnection.getInstance().getArticoli(categoria);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(event -> {
            List<Articolo> articoli = task.getValue();
            displayArticoli(articoli);
        });

        task.setOnFailed(event -> {
            System.out.println("Errore durante il caricamento degli articoli: " + task.getException());
        });
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