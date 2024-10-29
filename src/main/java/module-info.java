module it.unical.informatica.progettouid {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens it.unical.informatica.progettouid to javafx.fxml;
    exports it.unical.informatica.progettouid;
}