package it.unical.informatica.progettouid.model;

public class Client {
    private final int id;
    private final int nome;
    private final String cognome;
    private final String userId;


    public Client(int id, int nome, String cognome, String userId) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public int getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getUserId() {
        return userId;
    }
}
