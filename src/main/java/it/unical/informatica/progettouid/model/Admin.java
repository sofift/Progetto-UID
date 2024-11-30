package it.unical.informatica.progettouid.model;

public record Admin(int id, String nome, String cognome, String email, String password) {
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
    public String email() {
        return email;
    }

    @Override
    public String password() {
        return password;
    }
}
