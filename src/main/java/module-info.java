module it.unical.informatica.progettouid {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires spring.security.crypto;
    requires java.desktop;
    requires spring.security.core;
    requires java.management;
    requires java.prefs;

    opens it.unical.informatica.progettouid.controller to javafx.fxml;
    opens it.unical.informatica.progettouid to javafx.fxml;
    exports it.unical.informatica.progettouid;
    exports it.unical.informatica.progettouid.controller.Admin;
    exports it.unical.informatica.progettouid.controller.Trainer;
    exports it.unical.informatica.progettouid.controller.client;
    opens it.unical.informatica.progettouid.controller.client to javafx.fxml;
    opens it.unical.informatica.progettouid.controller.Admin to javafx.fxml;
    opens it.unical.informatica.progettouid.controller.Trainer to javafx.fxml;
    opens it.unical.informatica.progettouid.model to javafx.fxml;
}