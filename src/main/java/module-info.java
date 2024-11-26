module it.unical.informatica.progettouid {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires spring.security.crypto;
    requires java.desktop;

    opens it.unical.informatica.progettouid.controller to javafx.fxml;
    opens it.unical.informatica.progettouid to javafx.fxml;
    exports it.unical.informatica.progettouid;
    opens it.unical.informatica.progettouid.controller.client to javafx.fxml;
    opens it.unical.informatica.progettouid.model to javafx.fxml;
}