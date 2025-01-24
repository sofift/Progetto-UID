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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;

public class ArticoliController {
    @FXML private VBox vboxContent;
    private List<Articolo> articoliList = new ArrayList<>();

    private void displayArticoli() {
        vboxContent.getChildren().clear();
        ListView<HBox> listArticoli = new ListView<>();
        listArticoli.setFixedCellSize(90);
        listArticoli.setPrefHeight(articoliList.size() * listArticoli.getFixedCellSize() +5);
        VBox.setVgrow(listArticoli, Priority.ALWAYS);

        for(Articolo a : articoliList){
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
            visualizza.setOnAction(event -> displayArticoloSelezionato( a.titolo(), a.URL()));

            content.getChildren().addAll(textContainer, visualizza);
            listArticoli.getItems().add(content);
        }
        vboxContent.getChildren().add(listArticoli);
    }

    private void displayArticoloSelezionato(String titolo, String URL) {
        vboxContent.getChildren().clear();
        Button indietro = new Button();
        indietro.setGraphic(new FontIcon("fas-arrow-left"));
        indietro.setOnAction(e->{
            displayArticoli();
        });
        WebView webView = new WebView();
        VBox.setVgrow(webView, Priority.ALWAYS);
        webView.getEngine().load(URL);
        webView.setZoom(0.8);

        Label titoloLabel = new Label(titolo);

        vboxContent.getChildren().addAll(indietro, titoloLabel, webView);
    }

    public void loadContentPerCategoria(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String categoria = btn.getId();

        if(categoria.equals("TecnicheDiAllenamento"))    categoria = "Tecniche di allenamento";
        else if(categoria.equals("Nutrizione")) categoria = "Tecniche di allenamento";
        else if(categoria.equals("Riposo")) categoria = "Riposo";
        else if(categoria.equals("Recupero")) categoria = "Recupero";
        Task<List<Articolo>> task = DBConnection.getInstance().getArticoli(categoria);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        task.setOnSucceeded(event -> {
            articoliList = task.getValue();
            displayArticoli();
        });

        task.setOnFailed(event -> {
            System.out.println(STR."Errore durante il caricamento degli articoli: \{task.getException()}");
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