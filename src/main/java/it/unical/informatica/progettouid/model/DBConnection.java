package it.unical.informatica.progettouid.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
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


    public Task<Void> insertClient(String username, String cognome, String abbonamento, String datadinascita) {
        return asyncCall(() -> {
            if (isConnected()) {
                String query = "INSERT INTO Clienti (Username, Cognome, Abbonamento, DataNascita) VALUES(?, ?, ?, ?);";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, username);
                    stmt.setString(2, cognome);
                    stmt.setString(3, abbonamento);
                    stmt.setString(4, datadinascita);
                    stmt.executeUpdate();
                }
            }
            return null;
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
                    trainers.add(new PersonalTrainer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));

                }
                stmt.close();
            }
            return trainers;
        });
    }

    public Task<List<Corsi>> getCorsiDiOggi() {
        return asyncCall(() -> {
            String today = LocalDate.now().getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, Locale.ITALIAN)
                    .toLowerCase();
            List<Corsi> corsi = FXCollections.observableArrayList();
            if (isConnected()) {
                String query = "SELECT DISTINCT c.ID, c.Nome, c.Tipo, c.MaxPartecipanti, c.Durata, " +
                        "p.Nome as PTNome, o.oraInizio, o.oraFine " +
                        "FROM Corsi c " +
                        "JOIN OrarioCorsi o ON c.ID = o.idCorso " +
                        "JOIN PersonalTrainer p ON c.IdPersonal = p.ID " +
                        "WHERE o.giorno = ? " +
                        "GROUP BY c.ID, o.oraInizio " +  // Aggiunto GROUP BY
                        "ORDER BY o.oraInizio;";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, today);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    corsi.add(new Corsi(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4),
                            rs.getInt(5),
                            rs.getString(6),
                            rs.getString(7),
                            rs.getString(8)
                    ));

                }
                stmt.close();
            }
            return corsi;
        });
    }

    public Task<Abbonamento> getAccessiRimanenti(int idClient){
        return asyncCall(() -> {
            if (isConnected()) {
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
                        return new Abbonamento(
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

    public Task<Map<LocalDate, List<Corsi>>> getCorsiMensili(LocalDate inizio, LocalDate fine) {
        return asyncCall(() -> {
            Map<LocalDate, List<Corsi>> corsiPerGiorno = new HashMap<>();

            if (isConnected()) {
                String query = """
                    SELECT c.ID, c.Nome, c.Tipo, c.MaxPartecipanti, c.Durata,
                           p.Nome as PTNome, o.oraInizio, o.oraFine, o.giorno
                    FROM Corsi c
                    JOIN OrarioCorsi o ON c.ID = o.idCorso
                    JOIN PersonalTrainer p ON c.IdPersonal = p.ID
                    WHERE o.giorno IN ('lunedì', 'martedì', 'mercoledì', 'giovedì', 'venerdì', 'sabato', 'domenica')
                    ORDER BY o.oraInizio
                """;

                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            Corsi corso = new Corsi(
                                    rs.getInt("ID"),
                                    rs.getString("Nome"),
                                    rs.getString("Tipo"),
                                    rs.getInt("MaxPartecipanti"),
                                    rs.getInt("Durata"),
                                    rs.getString("PTNome"),
                                    rs.getString("oraInizio"),
                                    rs.getString("oraFine")
                            );

                            // Converti il giorno della settimana in date effettive del mese
                            String giorno = rs.getString("giorno");
                            List<LocalDate> date = getDatesForDayOfWeek(giorno, inizio, fine);

                            for (LocalDate data : date) {
                                corsiPerGiorno.computeIfAbsent(data, k -> new ArrayList<>()).add(corso);
                            }
                        }
                    }
                }
            }
            return corsiPerGiorno;
        });
    }

    private List<LocalDate> getDatesForDayOfWeek(String giornoSettimana, LocalDate inizio, LocalDate fine) {
        DayOfWeek dow = getDayOfWeek(giornoSettimana);
        List<LocalDate> dates = new ArrayList<>();

        LocalDate current = inizio;
        while (!current.isAfter(fine)) {
            if (current.getDayOfWeek() == dow) {
                dates.add(current);
            }
            current = current.plusDays(1);
        }

        return dates;
    }

    private DayOfWeek getDayOfWeek(String giorno) {
        return switch (giorno.toLowerCase()) {
            case "lunedì" -> DayOfWeek.MONDAY;
            case "martedì" -> DayOfWeek.TUESDAY;
            case "mercoledì" -> DayOfWeek.WEDNESDAY;
            case "giovedì" -> DayOfWeek.THURSDAY;
            case "venerdì" -> DayOfWeek.FRIDAY;
            case "sabato" -> DayOfWeek.SATURDAY;
            case "domenica" -> DayOfWeek.SUNDAY;
            default -> throw new IllegalArgumentException("Giorno non valido: " + giorno);
        };
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


    public Task<Trainer> authenticateTrainer(String email, String password) {
        return asyncCall(() -> {
            if (isConnected()) {
                String query = "SELECT * FROM PersonalTrainer WHERE email = ?;";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, email);
                    try (ResultSet rs = stmt.executeQuery()) {
                        String storedHash = rs.getString("Password");
                        if (rs.next() && BCrypt.checkpw(password, storedHash)) {

                            return new Trainer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
                        }
                    }
                }
            }
            return null;
        });
    }

    public Task<Admin> authenticateAdmin(String email, String password) {
        return asyncCall(() -> {
            if (isConnected()) {
                String query = "SELECT * FROM Admin WHERE email = ?;";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, email);
                    try (ResultSet rs = stmt.executeQuery()) {
                        String storedHash = rs.getString("Password");
                        if (rs.next() && BCrypt.checkpw(password, storedHash)) {

                            return new Admin(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
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
                String query = "SELECT * FROM Clienti WHERE email = ?;";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, email);
                    try (ResultSet rs = stmt.executeQuery()) {
                        String storedHash = rs.getString("Password");
                        if (rs.next() && BCrypt.checkpw(password, storedHash)) {

                            return new Client(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                        }
                    }
                }
            }
            return null;
        });
    }

}
    // CREAZIONE DELLA PLAYLIST AGGIUNTA DALL'UTENTE
/*
    public Task<Void> insertPlaylistFotUser(String username, String nome) {
        return asyncCall(() -> {
            if (isConnected()) {
                // Controlla se esiste già una playlist con lo stesso nome per questo utente
                PreparedStatement checkStmt = con.prepareStatement(
                        "SELECT COUNT(*) FROM Playlist WHERE nome = ? AND Username = ?"
                );
                checkStmt.setString(1, nome);
                checkStmt.setString(2, username);
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                int count = rs.getInt(1);
                rs.close();
                checkStmt.close();

                if (count > 0) {
                    // Playlist già esistente
                    System.out.println("Una playlist con questo nome esiste già per questo utente.");
                    return null;
                }

                // Se non esiste, procedi con l'inserimento
                PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO Playlist (nome, data_pubblicazione, Username, Copertina ) VALUES(?, ?, ?, ?);"
                );
                stmt.setString(1, nome);
                stmt.setString(2, String.valueOf(Date.valueOf(LocalDate.now())));
                stmt.setString(3, username);
                stmt.setString(4, "/Images/default.png");
                stmt.execute();
                stmt.close();
            }
            return null;
        });
    }

    // VISUALIZZARE LE PLAYLISY DELL'UTENTE

    public Task<List<Playlist>> getPlaylistPerUser(User user) {
        return asyncCall(() -> {
            List<Playlist> playlists = FXCollections.observableArrayList();
            if (isConnected()) {
                String username = user.username();
                String query = "SELECT id, nome, data_pubblicazione, copertina FROM Playlist WHERE Username=? or Username = 'Spotify';";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    playlists.add(new Playlist(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                }

                stmt.close();
            }
            return playlists;
        });
    }

    public Task<Boolean> checkPassword(String username, String password) {
        return asyncCall(() -> {
            if (isConnected()) {
                String query = "SELECT Password FROM Users WHERE Username = ?;";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, username);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String storedHash = rs.getString("Password");
                            return BCrypt.checkpw(password, storedHash);
                        }
                    }
                }
            }
            return false;
        });
    }


    public Task<Void> incrementaAscoltiPlaylist(int playlistId, String username) {
        return asyncCall(() -> {
            if (isConnected()) {
                String query = "INSERT INTO User_Playlist (Username, PlaylistID, ascolti, UltimoAscolto) " +
                        "VALUES (?, ?, 1, CURRENT_TIMESTAMP) " +
                        "ON CONFLICT (Username, PlaylistID) DO UPDATE SET ascolti = ascolti + 1, UltimoAscolto = CURRENT_TIMESTAMP;";

                // Aggiorna il contatore di ascolti, se esiste, altrimenti inserisce un nuovo record
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setInt(2, playlistId);
                stmt.executeUpdate();
                stmt.close();
            }
            return null;
        });
    }

    public Task<ObservableList<Playlist>> getTopPlaylistsForUser(String username, int limit) {
        return asyncCall(() -> {
            ObservableList<Playlist> playlists = FXCollections.observableArrayList();

            if (isConnected()) {
                String query =
                        "SELECT Playlist.nome, Playlist.Copertina " +
                                "FROM Playlist, User_Playlist " +
                                "WHERE Playlist.id = User_Playlist.PlaylistID AND User_Playlist.Username = ? " +
                                "ORDER BY User_Playlist.ascolti DESC, User_Playlist.UltimoAscolto DESC " +
                                "LIMIT ?";

                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setInt(2, limit);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Playlist playlist = new Playlist(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                    playlists.add(playlist);
                }
                rs.close();
                stmt.close();
            }
            return playlists;
        });
    }


    public Task<List<Brano>> cercaBrani(String nomeInserito) {
        return asyncCall(() -> {
            List<Brano> braniTrovati = new ArrayList<>();
            if (isConnected()) {
                String query = "select titolo, artista, album, durata FROM Brani WHERE titolo= ? or artista = ? or album = ?;";
                PreparedStatement stmt = con.prepareStatement(query);
                String queryParametri = "%" + query + "%";
                stmt.setString(1, queryParametri);
                stmt.setString(2, queryParametri);
                stmt.setString(3, queryParametri);
                stmt.setString(4, queryParametri);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    //braniTrovati.add(new Brano(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                }
                stmt.close();
            }
            return braniTrovati;
        });
    }

    public Task<ObservableList<BranoProperty>> showBrani(int playlistID, String username) {
        return asyncCall(() -> {
            ObservableList<BranoProperty> brani = FXCollections.observableArrayList();
            if (isConnected()) {
                String query = "SELECT titolo, artista, album, durata, percorso_file FROM Brani, Playlist_Brani, User_Playlist WHERE Brani.id = playlist_brani.brano_id and playlist_brani.playlist_id = user_playlist.PlaylistID and Username=? and user_playlist.PlaylistID = ?;";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setInt(1, playlistID);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    brani.add(new BranoProperty(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
                }
                stmt.close();
            }
            return brani;
        });

    }

}

*/