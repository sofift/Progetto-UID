package it.unical.informatica.progettouid.view;

import javafx.scene.control.Alert;

public class AlertManager extends Alert {

    public AlertManager(Alert.AlertType type, String title, String header, String content) {
        super(type);
        this.setTitle(title);
        this.setHeaderText(header);
        this.setContentText(content);
    }

    public void display() {
        this.showAndWait();
    }
}
