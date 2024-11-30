package it.unical.informatica.progettouid.model;

public record Corsi(int id, String nome, String tipo, int maxPartecipanti, int durata, String PT, String oraInizio, String oraFine) {
    @Override
    public int id() {
        return id;
    }

    @Override
    public String nome() {
        return nome;
    }

    @Override
    public String tipo() {
        return tipo;
    }

    @Override
    public int maxPartecipanti() {
        return maxPartecipanti;
    }

    @Override
    public int durata() {
        return durata;
    }

    @Override
    public String PT() {
        return PT;
    }

    @Override
    public String oraInizio() {
        return oraInizio;
    }

    @Override
    public String oraFine() {
        return oraFine;
    }
}
