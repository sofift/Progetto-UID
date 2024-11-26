package it.unical.informatica.progettouid.model;

import it.unical.informatica.progettouid.view.SceneHandlerClient;

public class SessionManager {
    private static SessionManager instance  = null;
    private Client loggedClient;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }


    public void setLoggedClient(Client client) {
        this.loggedClient = client;
    }

    public Client getLoggedClient() {
        if (loggedClient == null) {
            throw new IllegalStateException("Nessun client loggato");
        }
        return loggedClient;
    }

    public boolean isLoggedIn() {
        return loggedClient != null;
    }

    public void clearSession() {
        loggedClient = null;
    }
}