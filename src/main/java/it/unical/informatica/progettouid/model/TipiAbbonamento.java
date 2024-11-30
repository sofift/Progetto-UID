package it.unical.informatica.progettouid.model;

public record TipiAbbonamento(int id, String nome, String descrizione, int accessiTotali, int durataMesi, int prezzo, String dedicatoA) {
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
    public int accessiTotali() {
        return accessiTotali;
    }

    @Override
    public int durataMesi() {
        return durataMesi;
    }

    @Override
    public int prezzo() {
        return prezzo;
    }

    @Override
    public String dedicatoA() {
        return dedicatoA;
    }
}
