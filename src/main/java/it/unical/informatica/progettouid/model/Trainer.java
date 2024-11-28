package it.unical.informatica.progettouid.model;

public class Trainer {
    private final int id;
    private final String nome;
    private final String cognome;
    private final String dataDiNascita;
    private final String specializzazione;
    private final String email;
    private final String password;

    public Trainer(int id, String nome, String cognome, String dataDiNascita, String specializzazione, String email, String password) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = dataDiNascita;
        this.specializzazione = specializzazione;
        this.email = email;
        this.password = password;
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

    public String getDataDiNascita() {
        return dataDiNascita;
    }

    public String getSpecializzazione() {
        return specializzazione;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
