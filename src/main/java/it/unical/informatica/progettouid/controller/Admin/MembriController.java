package it.unical.informatica.progettouid.controller.Admin;
import it.unical.informatica.progettouid.model.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.kordamp.ikonli.javafx.FontIcon;

public class MembriController implements Initializable{
    @FXML
    private TableView<Client> tableView;

    @FXML
    private TableColumn<Client, String> membriColumn;

    @FXML
    private TableColumn<Client, String> contattiColumn;

    @FXML
    private TableColumn<Client, Integer> etaColumn;

    @FXML
    private TableColumn<Client, String> abbonamentoColumn;

    @FXML
    private TableColumn<Client, String> ultimaVisitaColumn;

    @FXML
    private TableColumn<Client, String> statoPagamentoColumn;

    @FXML
    private TableColumn<Client, Button> azioniColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Button filterButton;

    @FXML
    private Button addButton;

    @FXML
    private Button inviteButton;

    private ObservableList<Client> clientList;
    private FilteredList<Client> filteredData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeColumns();
        loadData();
        setupSearch();
        setupButtons();
    }

    private void initializeColumns() {
        membriColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        contattiColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        etaColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        abbonamentoColumn.setCellValueFactory(new PropertyValueFactory<>("subscription"));
        ultimaVisitaColumn.setCellValueFactory(new PropertyValueFactory<>("lastVisit"));
        statoPagamentoColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        // Configurazione colonna azioni
        azioniColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = createButton("fas-edit", "edit-button");
            private final Button deleteButton = createButton("fas-trash", "delete-button");

            {
                editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Creazione HBox per contenere i pulsanti
                    javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(5);
                    buttons.getChildren().addAll(editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    private Button createButton(String iconLiteral, String styleClass) {
        Button button = new Button();
        button.getStyleClass().add(styleClass);
        FontIcon icon = new FontIcon(iconLiteral);
        button.setGraphic(icon);
        return button;
    }

    private void loadData() {
        // Qui dovresti caricare i dati dal tuo database o servizio
        clientList = FXCollections.observableArrayList();
        // Esempio: clientList.addAll(clientService.getAllClients());
        tableView.setItems(clientList);
    }

    private void setupSearch() {
        filteredData = new FilteredList<>(clientList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(client -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return client.nome().toLowerCase().contains(lowerCaseFilter) ||
                        client.email().toLowerCase().contains(lowerCaseFilter);
            });
            tableView.setItems(filteredData);
        });
    }

    private void setupButtons() {
        filterButton.setOnAction(event -> handleFilter());
        addButton.setOnAction(event -> handleAddMember());
        inviteButton.setOnAction(event -> handleInvite());
    }

    private void handleFilter() {
        // Implementa la logica del filtro avanzato
        // Potresti aprire una finestra di dialogo con opzioni di filtro più dettagliate
    }

    private void handleAddMember() {
        // Implementa la logica per aggiungere un nuovo membro
        // Potresti aprire una finestra di dialogo con un form
    }

    private void handleInvite() {
        // Implementa la logica per inviare inviti
        // Potresti aprire una finestra di dialogo per l'invio di email
    }

    private void handleEdit(Client client) {
        if (client != null) {
            // Implementa la logica per modificare un membro
            // Potresti aprire una finestra di dialogo con i dati del cliente da modificare
        }
    }

    private void handleDelete(Client client) {
        if (client != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma eliminazione");
            alert.setHeaderText("Stai per eliminare il membro: " + client.nome() + client.cognome());
            alert.setContentText("Sei sicuro di voler procedere?");

            alert.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    // Implementa la logica di eliminazione
                    clientList.remove(client);
                    // clientService.deleteClient(client.getId());
                }
            });
        }
    }
}
