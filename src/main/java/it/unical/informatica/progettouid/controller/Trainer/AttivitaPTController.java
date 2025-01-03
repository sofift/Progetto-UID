package it.unical.informatica.progettouid.controller.Trainer;


import it.unical.informatica.progettouid.view.SceneHandlerPT;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AttivitaPTController {

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