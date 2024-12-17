package it.unical.informatica.progettouid.model;

public record Corsi(int id, String nome, String descrizione, int durata, String PT) {
    @Override
    public int id() {
        return id;
    }

    @Override
    public String nome() {
        return nome;
    }

    @Override
    public String descrizione() {
        return descrizione;
    }

    @Override
    public int durata() {
        return durata;
    }

    @Override
    public String PT() {
        return PT;
    }

}
