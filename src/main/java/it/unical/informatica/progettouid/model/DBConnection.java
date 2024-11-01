package it.unical.informatica.progettouid.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    public Task<Void> insertUser(String username, String email, String datanascita, String password) {
        return asyncCall(() -> {
            if (isConnected()) {
                String query = "INSERT INTO Users (Username, Email, DataNascita, Password) VALUES(?, ?, ?, ?);";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, username);
                    stmt.setString(2, email);
                    stmt.setString(3, datanascita);
                    stmt.setString(4, password);
                    stmt.executeUpdate();
                }
            }
            return null;
        });
    }

    // CREAZIONE DELLA PLAYLIST AGGIUNTA DALL'UTENTE

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

