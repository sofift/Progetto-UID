package it.unical.informatica.progettouid.model;

public record Corsi(int id, String nome, String descrizione, int idPT, String nomeTrainer, String cognomeTrainer) {
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
    public int idPT() {
        return idPT;
    }

    @Override
    public String nomeTrainer() {
        return nomeTrainer;
    }

    @Override
    public String cognomeTrainer() {
        return cognomeTrainer;
    }
}
