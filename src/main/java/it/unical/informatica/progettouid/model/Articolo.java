package it.unical.informatica.progettouid.model;

public record Articolo(int id, String titolo, String descrizione, String categoria, String URL){
    @Override
    public int id() {
        return id;
    }

    @Override
    public String titolo() {
        return titolo;
    }

    @Override
    public String descrizione() {
        return descrizione;
    }

    @Override
    public String categoria() {
        return categoria;
    }

    @Override
    public String URL() {
        return URL;
    }
}
