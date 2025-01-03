package it.unical.informatica.progettouid.model;

public record Client(int id, String nome, String cognome, String dataNascita, String abbonamento, String email) {
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
    public String abbonamento() {
        return abbonamento;
    }

    @Override
    public String email() {
        return email;
    }
}
