package it.unical.informatica.progettouid.model;

public record Client(int id, String nome, String cognome, String abbonamento, String dataNascita, int idUser) {
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
    public String abbonamento() {
        return abbonamento;
    }

    @Override
    public String dataNascita() {
        return dataNascita;
    }

    @Override
    public int idUser() {
        return idUser;
    }
}
