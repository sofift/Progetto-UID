package it.unical.informatica.progettouid.model;

public record PrenotazionePT(int idPT, String nomeClient, String cognomeClient, String data, String oraPrenotazione, String notes) {
    @Override
    public String nomeClient() {
        return nomeClient;
    }

    @Override
    public int idPT() {
        return idPT;
    }

    @Override
    public String cognomeClient() {
        return cognomeClient;
    }

    @Override
    public String data() {
        return data;
    }

    @Override
    public String oraPrenotazione() {
        return oraPrenotazione;
    }

    @Override
    public String notes() {
        return notes;
    }
}
