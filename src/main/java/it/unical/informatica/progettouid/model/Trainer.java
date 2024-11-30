package it.unical.informatica.progettouid.model;

public record Trainer(int id, String nome, String cognome, String dataDiNascita, String specializzazione, String email, String password) {
    @Override
    public int id() {
        return id;
    }

    @Override
    public String nome() {
        return nome;
    }

    @Override
    public String cognome() {
        return cognome;
    }

    @Override
    public String dataDiNascita() {
        return dataDiNascita;
    }

    @Override
    public String specializzazione() {
        return specializzazione;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String password() {
        return password;
    }
}
