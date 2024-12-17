package it.unical.informatica.progettouid.model;

public record Notifica(int id, Integer destID, String message, String stato) {
    @Override
    public int id() {
        return id;
    }

    @Override
    public Integer destID() {
        return destID;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String stato() {
        return stato;
    }
}
