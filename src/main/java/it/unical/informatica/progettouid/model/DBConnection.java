package it.unical.informatica.progettouid.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.*;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* Mi creo una classe singleton per avere una sola istanza del database e utilizzo l'ExecutorService
* per gestire il database in maniera asincrona */

public class DBConnection {
    private Connection con = null;
    private static final DBConnection instance = new DBConnection();        // classe Singleton
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public static DBConnection getInstance() {
        return instance;
    }

    // Effettua la connessione con il database con jdbc
    private DBConnection() {
        try {
            String url = "jdbc:sqlite:dbGym.db";
            con = DriverManager.getConnection(url);
            if (isConnected())
                System.out.println("Connected!");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    // Verifica se la connessione è attiva
    public boolean isConnected() {
        try {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    // Chiusura connessione
    private void close() {
        executorService.shutdown();
        try {
            if (con != null)
                con.close();
        } catch (SQLException ignored) {
        }
    }

    public Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                // Modifica l'URL per il tuo database
                String url = "jdbc:sqlite:your-database.db";
                con = DriverManager.getConnection(url);
                System.out.println("Connessione al database SQLite aperta.");
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'apertura della connessione al database:");
            e.printStackTrace();
        }
        return con;
    }

    // Effettua una chiamata asincrona a un thread
    private <T> Task<T> asyncCall(Callable<T> callable) {
        return new Task<>() {
            @Override
            protected T call() throws Exception {
                return callable.call();
            }
        };
    }

    public void removeAllUser() throws SQLException {
        if (isConnected()) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE from users;");
            stmt.close();
        }
    }

    public Task<Boolean> insertAdmin(String nome, String cognome, String username, String password) {
        return asyncCall(() -> {
            if (isConnected()) {
                System.out.println("Inserimento Admin: " + nome + " " + cognome);
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                String query = "INSERT INTO Admin (Nome, Cognome, Username, Password) VALUES(?, ?, ?, ?);";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, nome);
                    stmt.setString(2, cognome);
                    stmt.setString(3, username);
                    stmt.setString(4, hashedPassword);
                    stmt.execute();
                    return true;
                }
            }
            return false;
        });
    }

    public Task<Boolean> insertClient(String nome, String cognome, String datadinascita, String email, String password) {
        return asyncCall(() -> {
            if (isConnected()) {
                System.out.println("Connessione al database avvenuta con successo.");
                System.out.println("Inserimento Client: " + nome + " " + cognome + " " + email);
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                String query = "INSERT INTO Clienti (Nome, Cognome, DataNascita, Abbonamento, email, password) VALUES(?, ?, ?, ?, ?, ?);";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, nome);
                    stmt.setString(2, cognome);
                    stmt.setString(3, datadinascita);
                    stmt.setString(4, null);
                    stmt.setString(5, email);
                    stmt.setString(6, hashedPassword);
                    stmt.execute();
                    return true;
                }
            }
            return false;
        });
    }

    public Task<Boolean> insertTrainer(String nome, String cognome, String datadinascita, String specializzazione, String email, String password, String telefono) {
        return asyncCall(() -> {
            if (isConnected()) {
                System.out.println("Connessione al database avvenuta con successo.");
                System.out.println("Inserimento Client: " + nome + " " + cognome + " " + email);
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                String query = "INSERT INTO PersonalTrainer (Nome, Cognome, DataNascita, Specializzazione, Email, Password, Telefono) VALUES(?, ?, ?, ?, ?, ?, ?);";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, nome);
                    stmt.setString(2, cognome);
                    stmt.setString(3, datadinascita);
                    stmt.setString(4, specializzazione);
                    stmt.setString(5, email);
                    stmt.setString(6, hashedPassword);
                    stmt.setString(7, telefono);
                    stmt.execute();
                    return true;
                }
            }
            return false;
        });
    }

    public Task<List<Client>> getAllClient() {
        return asyncCall(() -> {
            List<Client> clients = FXCollections.observableArrayList();
            if (isConnected()) {
                String query = "SELECT * FROM Clienti ;";
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    clients.add(new Client(
                            rs.getInt("Id"),
                            rs.getString("Nome"),
                            rs.getString("Cognome"),
                            rs.getString("Abbonamento"),
                            rs.getString("DataNascita"),
                            rs.getString("Email")
                    ));

                }
                stmt.close();
            }
            return clients;
        });
    }

    // get personal trainer
    public Task<List<PersonalTrainer>> getAllPt() {
        return asyncCall(() -> {
            List<PersonalTrainer> trainers = FXCollections.observableArrayList();
            if (isConnected()) {
                String query = "SELECT * FROM PersonalTrainer ;";
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    trainers.add(new PersonalTrainer(
                            rs.getInt("id"),
                            rs.getString("Nome"),
                            rs.getString("Cognome"),
                            rs.getString("DataNascita"),
                            rs.getString("Specializzazione"),
                            rs.getString("Email"),
                            rs.getString("Telefono")));
                }
                stmt.close();
            }
            return trainers;
        });
    }

    public Task<PersonalTrainer> getInfoPT() {
        return asyncCall(() -> {
            if (isConnected()) {
                int idClient = ClientSession.getInstance().getCurrentClient().getId();
                String query = "SELECT pt.nome, pt.cognome, pt.DataNascita, pt.specializzazione, pt.email, pt.telefono" +
                        " FROM PersonalTrainer pt, Schede s " +
                        "WHERE s.ClienteID = ?;";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, idClient);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new PersonalTrainer(
                            rs.getInt("id"),
                            rs.getString("Nome"),
                            rs.getString("Cognome"),
                            rs.getString("DataNascita"),
                            rs.getString("Specializzazione"),
                            rs.getString("Email"),
                            rs.getString("Telefono"));

                }
                stmt.close();
            }
            return null;
        });
    }

    public Task<List<Corsi>> getCorsi() {
        return asyncCall(() -> {
            List<Corsi> corsi = FXCollections.observableArrayList();
            if (isConnected()) {
                String query = "SELECT *" +
                        "FROM Corsi c ";
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    corsi.add(new Corsi(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4),
                            rs.getString(5)
                    ));

                }
                stmt.close();
            }
            return corsi;
        });
    }

    public Task<List<Corsi>> getCorsiDiOggi() {
        return asyncCall(() -> {
            String today = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ITALIAN).toLowerCase();
            List<Corsi> corsi = FXCollections.observableArrayList();
            if (isConnected()) {
                String query = "SELECT c.ID, c.Nome, c.Descrizione, c.idTrainer " +
                        "FROM Corsi c JOIN OrarioCorsi oc ON c.ID = oc.idCorso " +
                        "WHERE oc.giorno = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, today);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    corsi.add(new Corsi(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4),
                            rs.getString(5)
                    ));

                }
                stmt.close();
            }
            return corsi;
        });
    }

    public Task<List<OrariCorsi>> getOrarioCorsi(int idCorso){
        return asyncCall(() -> {
            List<OrariCorsi> orari = FXCollections.observableArrayList();
            if (isConnected()) {
                String query = "SELECT c.id, c.nome, oc.giorno, oc.oraInizio, oc.oraFine " +
                        "FROM Corsi c " +
                        "JOIN OrarioCorsi oc ON oc.idCorso = c.ID " +
                        "WHERE c.ID = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, idCorso);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    orari.add(new OrariCorsi(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5)
                    ));

                }
                stmt.close();
            }
            return orari;
        });
    }

    public Task<InfoAccessiAbbonamento> getAccessiRimanenti(){
        return asyncCall(() -> {
            if (isConnected()) {
                int idClient = ClientSession.getInstance().getCurrentClient().getId();
                String query = "SELECT a.AccessiRimanenti, ta.NumeroAccessiTotali, a.DataScadenza, ta.Nome as TipoAbbonamento " +
                        "FROM Abbonamenti a " +
                        "JOIN TipiAbbonamento ta ON a.TipoAbbonamentoID = ta.ID " +
                        "WHERE a.ClienteID = ? " +
                        "AND a.Stato = 'attivo' " +
                        "AND a.DataScadenza >= date('now') " +
                        "ORDER BY a.DataScadenza DESC " +
                        "LIMIT 1";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, idClient);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new InfoAccessiAbbonamento(
                                rs.getInt(1),
                                rs.getInt(2),
                                rs.getString(3),
                                rs.getString(4)
                        );
                    }
                }
                stmt.close();
            }
            return null;
        });
    }

    public Task<InfoBaseAbbonamento> getInfoAbbonamento(){
        return asyncCall(() -> {
            if (isConnected()) {
                int idClient = ClientSession.getInstance().getCurrentClient().getId();
                String query = "SELECT ta.Nome, a.dataInizio, a.dataScadenza, a.stato, ta.descrizione " +
                        "FROM Abbonamenti a " +
                        "JOIN TipiAbbonamento ta ON a.TipoAbbonamentoID = ta.ID " +
                        "WHERE a.ClienteID = ? ";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, idClient);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new InfoBaseAbbonamento(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5)
                        );
                    }
                }
                stmt.close();
            }
            return null;
        });
    }

    public Task<List<TipiAbbonamento>> getAllPianiAbbonamento() {
        return asyncCall(() -> {
            List<TipiAbbonamento> piani = FXCollections.observableArrayList();
            if (isConnected()) {
                String query = "SELECT * FROM TipiAbbonamento ;";
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    piani.add(new TipiAbbonamento(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getString(7)));

                }
                stmt.close();
            }
            return piani;
        });
    }

    public Task<SchedaAllenamento> getInfoSchedaClient() {
        return asyncCall(() -> {
            if (isConnected()) {
                int idClient = ClientSession.getInstance().getCurrentClient().getId();
                String query = "SELECT s.*, pt.Nome, pt.Cognome" +
                        "                FROM Schede s" +
                        "                JOIN PersonalTrainer pt ON s.PersonalTrainerID = pt.ID" +
                        "                WHERE s.ClienteID = ?" +
                        "                AND s.Stato = 'attiva'" +
                        "                AND s.DataFine >= date('now')" +
                        "                LIMIT 1";
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new SchedaAllenamento(
                            rs.getInt("id"),
                            rs.getString("Nome"),
                            rs.getString("Cognome"),
                            rs.getString("DataInizio"),
                            rs.getString("DataFine"),
                            rs.getString("Obiettivi"),
                            rs.getString("NoteGenerali"),
                            rs.getString("SuggerimentiAlimentari"));
                }
                stmt.close();
            }
            return null;
        });
    }

    public Task<ObservableList<Esercizio>> getEserciziGiorno(int clientID, String giorno) {
        return asyncCall(() -> {
            ObservableList<Esercizio> esercizi = FXCollections.observableArrayList();
            if (isConnected()) {
                String query = " SELECT es.*, e.Nome, e.Descrizione, e.GruppoMuscolare, e.LivelloDifficolta " +
                        "FROM EserciziScheda es " +
                        "JOIN Esercizi e ON es.EsercizioID = e.ID " +
                        "JOIN Schede s ON s.ID = es.SchedaID " +
                        "WHERE s.clientID = ? AND es.GiornoSettimana = ? AND " +
                        "ORDER BY es.OrdineEsecuzione ";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setInt(1, clientID);
                    stmt.setString(2, giorno);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while(rs.next()) {
                            esercizi.add( new Esercizio(
                                    rs.getInt("NumeroSerie"),
                                    rs.getInt("NumeroRipetizioni"),
                                    rs.getInt("TempoRecupero"),
                                    rs.getString("Note"),
                                    rs.getString("Nome"),
                                    rs.getString("Descrizione"),
                                    rs.getString("GruppoMuscolare"),
                                    rs.getString("LivelloDifficolta")));
                        }
                    }
                }

            }
            return esercizi;
        });
    }

    public Task<PersonalTrainer> authenticateTrainer(String email, String password) {
        return asyncCall(() -> {
            if (isConnected()) {
                String query = "SELECT * FROM PersonalTrainer WHERE email = ?;";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, email);
                    try (ResultSet rs = stmt.executeQuery()) {
                        String storedHash = rs.getString("Password");
                        if (rs.next() && BCrypt.checkpw(password, storedHash)) {

                            return new PersonalTrainer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
                        }
                    }
                }
            }
            return null;
        });
    }

    public Task<Admin> authenticateAdmin(String username, String password) {
        return asyncCall(() -> {
            if (isConnected()) {
                String query = "SELECT * FROM Admin WHERE username = ?;";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, username);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {  // First move to the row
                            String storedHash = rs.getString("Password");
                            if (BCrypt.checkpw(password, storedHash)) {
                                return new Admin(
                                        rs.getInt("ID"),
                                        rs.getString("Nome"),
                                        rs.getString("Cognome"),
                                        rs.getString("Username"),
                                        rs.getString("Password")
                                );
                            }
                        }
                    }
                }
            }
            return null;
        });
    }

    public Task<Client> authenticateUser(String email, String password) {
        return asyncCall(() -> {
            if (isConnected()) {
                String query = "SELECT *" +
                        "FROM Clienti " +
                        "WHERE email = ?;";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, email);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String storedHash = rs.getString("Password");
                            if (BCrypt.checkpw(password, storedHash)) {
                                return new Client(
                                        rs.getInt("Id"),
                                        rs.getString("Nome"),
                                        rs.getString("Cognome"),
                                        rs.getString("Abbonamento"),
                                        rs.getString("DataNascita"),
                                        rs.getString("Email")
                                );
                            }
                        }
                    }
                }
            }
            return null;
        });
    }

    public Task<Boolean> insertPrenotazionePT(int trainerId, String data, String oraPrenotazione, String notes) {
        return asyncCall(() -> {
            if (isConnected()) {
                int idClient = ClientSession.getInstance().getCurrentClient().getId();
                String query = "INSERT INTO PrenotazioniPT (idPT, idClient, data, oraPrenotazione, notes) VALUES(?, ?, ?, ?, ?);";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setInt(1, trainerId);
                    stmt.setInt(2, idClient);
                    stmt.setString(3, data);
                    stmt.setString(4, oraPrenotazione);
                    stmt.setString(5, notes);
                    stmt.executeUpdate();
                    return true;
                }
            }
            return false;
        });
    }

    public Task<Void> addEsercizioScheda(EsercizioScheda esercizioScheda) {
        return asyncCall(() -> {
            if (isConnected()) {
                int idClient = ClientSession.getInstance().getCurrentClient().getId();
                String queryIDScheda = "SELECT ID FROM Schede where ClienteID = ?";

                int idScheda;

                try (PreparedStatement stmt = con.prepareStatement(queryIDScheda)) {
                    stmt.setInt(1, idClient);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            throw new SQLException("scheda non trovato: " );
                        }
                        idScheda = rs.getInt("ID");
                    }
                }
                String queryEsercizio = "SELECT ID FROM Esercizi WHERE Nome = ?;";
                int esercizioId;

                try (PreparedStatement stmtEsercizio = con.prepareStatement(queryEsercizio)) {
                    stmtEsercizio.setString(1, esercizioScheda.nomeEserc());
                    ResultSet rs = stmtEsercizio.executeQuery();
                    if (!rs.next()) {
                        throw new SQLException("Esercizio non trovato: " + esercizioScheda.nomeEserc());
                    }
                    esercizioId = rs.getInt("ID");
                }

                String query = "INSERT INTO EserciziScheda (SchedaID, EsercizioID, " +
                        "NumeroSerie, NumeroRipetizioni, TempoRecupero, Note) " +
                        "VALUES (?, ?, ?, ?, ?, ?);";

                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setInt(1, idScheda);
                    stmt.setInt(2, esercizioId);
                    stmt.setString(3, esercizioScheda.nSerie());
                    stmt.setString(4, esercizioScheda.nRipetizioni());
                    stmt.setString(5, esercizioScheda.tmpRecupero());
                    stmt.setString(6, esercizioScheda.notes());
                    stmt.executeUpdate();
                }

            }
            return null;
        });
    }

    public Task<List<PrenotazionePT>> getPrenotazioneTrainer(){
        return asyncCall(() -> {
            List<PrenotazionePT> prenotazioni = new ArrayList<>();
            if (isConnected()) {
                int idPT = PTSession.getInstance().getCurrentTrainer().getId();
                String query = "SELECT c.nome, c.cognome, p.data, p.oraPrenotazione, p.notes" +
                        "FROM PrenotazioniPT p " +
                        "JOIN Clienti c ON c.id = p.idClient" +
                        "WHERE p.idPT = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, idPT);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        prenotazioni.add( new PrenotazionePT(
                                rs.getInt(idPT),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5),
                                rs.getString(6)));
                    }
                }
                stmt.close();
            }
            return prenotazioni;
        });
    }

    public Task<Void> insertNotifyTrainer(int trainerID, String message) {
        return asyncCall(() -> {
            if (isConnected()) {
                String query = "INSERT INTO NotifichePT (trainerId, message, status) VALUES(?, ?, ?);";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setInt(1, trainerID);
                    stmt.setString(2, message);
                    stmt.setString(3, "non letta");
                    stmt.executeUpdate();

                }
            }
            return null;
        });
    }

    public Task<Void> insertNotifyClient(String message) {
        return asyncCall(() -> {
            if (isConnected()) {
                String query = "INSERT INTO NotificheClient (message, status) VALUES(?, ?);";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, message);
                    stmt.setString(2, "attiva");
                    stmt.executeUpdate();

                }
            }
            return null;
        });
    }

    public Task<List<Notifica>> getNotificheNonLette() {
        return asyncCall(() -> {
            List<Notifica> notifiche = new ArrayList<>();
            if (isConnected()) {
                int trainerID = PTSession.getInstance().getCurrentTrainer().getId();
                String query = "SELECT * FROM Notifiche WHERE trainerId = ? AND stato = 'non letta'";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, trainerID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        notifiche.add( new Notifica(
                                rs.getInt(1),
                                rs.getInt(2),
                                rs.getString(3),
                                rs.getString(4)));
                    }
                }
                stmt.close();
            }
            return notifiche;
        });
    }

    public Task<List<Notifica>> getNotificheClient() {
        return asyncCall(() -> {
            List<Notifica> notifiche = new ArrayList<>();
            if (isConnected()) {
                String query = "SELECT * FROM NotificheClient ";
                PreparedStatement stmt = con.prepareStatement(query);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        notifiche.add( new Notifica(
                                rs.getInt(1),
                                null,
                                rs.getString(2),
                                rs.getString(3)));
                    }
                }
                stmt.close();
            }
            return notifiche;
        });
    }

    public Task<Void> updateNotifyPT(int idNotify, String stato) {
        return asyncCall(() -> {
            if (isConnected()) {
                String query = "UPDATE Notifiche SET stato = ? WHERE id = ?";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, stato);
                    stmt.setInt(2, idNotify);
                    stmt.executeUpdate();
                }
            }
            return null;
        });
    }

    public Task<Boolean> clientHaUnaScheda(int idClient) {
        return asyncCall(() -> {
            if (isConnected()) {

                String query = "SELECT COUNT(*) FROM Schede WHERE ClienteID = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, idClient);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                stmt.close();
            }
            return null;
        });
    }

    public Task<Void> insertPagamento(int idAbbonamento, int importo, String stato) {
        return asyncCall(() -> {
            if (isConnected()) {
                int idClient = ClientSession.getInstance().getCurrentClient().getId();
                LocalDate today = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                String formattedDate = today.format(formatter);
                String query = "INSERT INTO Pagamenti (idClient, idAbbonamento, importo, data, stato) VALUES(?, ?, ?, ?, ?);";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setInt(1, idClient);
                    stmt.setInt(2, idAbbonamento);
                    stmt.setInt(3,  importo);
                    stmt.setString(4, formattedDate);
                    stmt.setString(5, stato);
                    stmt.executeUpdate();

                }
            }
            return null;
        });
    }

    // far modificare il giorno relativo al giorno della settimana in data effettiva
    private String getDataDaGiorno(String giorno) {
        Map<String, DayOfWeek> giorni = Map.of(
                "lunedì", DayOfWeek.MONDAY,
                "martedì", DayOfWeek.TUESDAY,
                "mercoledì", DayOfWeek.WEDNESDAY,
                "giovedì", DayOfWeek.THURSDAY,
                "venerdì", DayOfWeek.FRIDAY,
                "sabato", DayOfWeek.SATURDAY,
                "domenica", DayOfWeek.SUNDAY
        );
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        DayOfWeek targetDay = giorni.get(giorno.toLowerCase());
        LocalDate targetDate = startOfWeek.with(TemporalAdjusters.nextOrSame(targetDay));
        return targetDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public Task<Void> insertPrenotazioneCorso(int idCorso, String giorno) {
        return asyncCall(() -> {
            if (isConnected()) {
                String dataCorso = getDataDaGiorno(giorno);
                int idClient = ClientSession.getInstance().getCurrentClient().getId();
                String query = "INSERT INTO PrenotazioniCorsi (idCorso, idClient, dataPrenotazione) VALUES (?, ?, ?);";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setInt(1, idCorso);
                    stmt.setInt(2, idClient);
                    stmt.setDate(3, Date.valueOf(dataCorso));
                    stmt.executeUpdate();
                }
            }
            return null;
        });
    }

    public Task<Boolean> checkIn(String code) {
        return asyncCall(() -> {
            if (isConnected()) {
                String verifyquery = """
                        SELECT c.Id, a.AccessiRimanenti\s
                                        FROM Clienti c
                                        LEFT JOIN Abbonamenti a ON c.Id = a.ClienteID\s
                                        WHERE c.Id = ?\s
                                        AND a.Stato = 'attivo'\s
                                        AND a.DataScadenza >= date('now')
                                        AND a.AccessiRimanenti > 0 """;
                try (PreparedStatement stmt = con.prepareStatement(verifyquery)) {
                    stmt.setString(1, code);
                    ResultSet rs = stmt.executeQuery();

                    if (!rs.next()) {
                        throw new IllegalArgumentException("Codice non valido o abbonamento scaduto.");
                    }
                    int clientId = rs.getInt("Id");
                    int accessiRimanenti = rs.getInt("AccessiRimanenti");

                    String insertQuery = "INSERT INTO AccessiPalestra (ClienteID, DataOraIngresso) VALUES (?, datetime('now', 'localtime'))";
                    try (PreparedStatement stmt2 = con.prepareStatement(insertQuery)) {
                        stmt2.setInt(1, clientId);
                        stmt2.executeUpdate();
                    }

                    String updateQuery = "UPDATE Abbonamenti SET AccessiRimanenti = ? WHERE ClienteID = ? AND Stato = 'attivo'";
                    try (PreparedStatement stmt3 = con.prepareStatement(updateQuery)) {
                        stmt3.setInt(1, accessiRimanenti - 1);
                        stmt3.setInt(2, clientId);
                        stmt3.executeUpdate();
                    }

                    return true;
                }
            }
            return false;
        });
    }

    /*public LocalDate getInizioSettimana() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        int daysFromMonday = dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
        return today.minusDays(daysFromMonday);
    }

    public Task<Void> aggiornaConteggioPresenzeCorsi(int idCorso, String data){
        return asyncCall(() -> {
            if (isConnected()) {
                LocalDate inizioSettimana = getInizioSettimana();
                String query = "INSERT INTO ConteggioPrenotazioni (idCorso, dataPrenotazione, contatore) VALUES (?, ?, 1) ON DUPLICATE KEY UPDATE contatore = contatore + 1;";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setInt(1, idCorso);
                    stmt.setDate(2, Date.valueOf(inizioSettimana));
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });

    }

    public Task<List<ConteggioCorso>> getConteggiSettimanali(int idCorso){
        return asyncCall(()->{
           if(isConnected()){
               List<ConteggioCorso> conteggi = new ArrayList<>();
               LocalDate inizioSettimana = getInizioSettimana();
               String query = "SELECT contatore FROM ConteggioPrenotazioni WHERE idCorso = ? AND settimanaCorrente = ?";
               try (PreparedStatement stmt = con.prepareStatement(query)) {
                   stmt.setInt(1, idCorso);
                   stmt.setDate(2, Date.valueOf(inizioSettimana));
                   try (ResultSet rs = stmt.executeQuery()) {
                       if (rs.next()) {
                           conteggi.add(new ConteggioCorso(idCorso, inizioSettimana, rs.getInt("contatore")));
                       }
                   }
               } catch (SQLException e) {
                   e.printStackTrace();
               }
               return conteggi;
           }
        });
    }*/
}
