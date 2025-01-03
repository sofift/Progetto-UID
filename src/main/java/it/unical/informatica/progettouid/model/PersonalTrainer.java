package it.unical.informatica.progettouid.model;

public record PersonalTrainer(int id, String nome, String cognome, String dataNascita, String specializzazione, String email, String telefono) {
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
    public String dataNascita() {
        return dataNascita;
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
    public String telefono() {
        return telefono;
    }
}


