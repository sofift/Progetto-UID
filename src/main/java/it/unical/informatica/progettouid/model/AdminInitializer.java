package it.unical.informatica.progettouid.model;
import it.unical.informatica.progettouid.util.AdminPasswordGenerator;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminInitializer {

    public boolean areAdminsInitialized() {
        String query = "SELECT COUNT(*) AS total FROM admin";
        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("total");
                System.out.println("Totale degli admini: " + count);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void initializeAdmins() {
        try {
            if (!DBConnection.getInstance().isConnected()) {
                System.out.println("Errore: Connessione al database non riuscita.");
                return;
            }

            System.out.println("Connessione al database riuscita. Inizializzazione degli admin...");

            if (!areAdminsInitialized()) {
                String query = "INSERT INTO Admin (ID, Nome, Cognome, Username, Password) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query)) {

                    // Admin 1
                    stmt.setString(2, "Sara");
                    stmt.setString(3, "Verdi");
                    stmt.setString(4, "Admin1");
                    stmt.setString(5, AdminPasswordGenerator.generateHashedPassword("adminPassword1"));
                    int rowsInserted1 = stmt.executeUpdate();
                    System.out.println("Admin 1 inserito. Righe modificate: " + rowsInserted1);

                    // Admin 2
                    stmt.setString(2, "Andrea");
                    stmt.setString(3, "Bianchi");
                    stmt.setString(4, "Admin2");
                    stmt.setString(5, AdminPasswordGenerator.generateHashedPassword("adminPassword2"));
                    int rowsInserted2 = stmt.executeUpdate();
                    System.out.println("Admin 2 inserito. Righe modificate: " + rowsInserted2);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
}

