package it.unical.informatica.progettouid.model;

public class Client {
    private final int id;
    private final String nome;
    private final String cognome;
    private final String email;
    //private final String password;



    public Client(int id, String nome, String cognome, String email /*String password*/) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        //his.password = password;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }


}
