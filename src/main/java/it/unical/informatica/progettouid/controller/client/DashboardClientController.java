package it.unical.informatica.progettouid.controller.client;

import it.unical.informatica.progettouid.view.SceneHandlerClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;


public class DashboardClientController {
    @FXML
    private BorderPane mainPane;

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
                case "prenotazioneClient":
                    SceneHandlerClient.getInstance().setPrenotazioniView();
                    break;
                case "schedaClient":
                    SceneHandlerClient.getInstance().setSchedaView();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
