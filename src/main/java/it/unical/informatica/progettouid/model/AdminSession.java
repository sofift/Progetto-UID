package it.unical.informatica.progettouid.model;

public class AdminSession {
    private static AdminSession instance;
    private Admin loggedAdmin;

    private AdminSession() {}

    public static AdminSession getInstance() {
        if (instance == null) {
            instance = new AdminSession();
        }
        return instance;
    }

    public void setCurrentAdmin(Admin admin) {
        this.loggedAdmin = admin;
    }

    public Admin getCurrentAdmin() {
        if (loggedAdmin == null) {
            throw new IllegalStateException("Nessun amministratore loggato");
        }
        return loggedAdmin;
    }

    public void logout() {
        loggedAdmin = null;
    }
}
