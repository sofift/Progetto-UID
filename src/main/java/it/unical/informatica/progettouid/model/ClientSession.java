package it.unical.informatica.progettouid.model;

public class ClientSession{
    private static ClientSession instance;
    private Client loggedClient;

    private ClientSession(){}

    public static ClientSession getInstance(){
        if(instance == null){
            instance = new ClientSession();
        }
        return instance;
    }

    public void setCurrentClient(Client client) {
        this.loggedClient = client;
    }

    public Client getCurrentClient() {
        if (loggedClient == null) {
            throw new IllegalStateException("Nessun client loggato");
        }
        return loggedClient;
    }


    public void logout() {
        loggedClient = null;
    }
}