package it.unical.informatica.progettouid.model;

public record PrenotazionePT(int idPrenotazione, int idPT, String nomeClient, String cognomeClient, String data, String oraPrenotazione, String notes, String stato) {
    @Override
    public String nomeClient() {
        return nomeClient;
    }

    @Override
    public int idPrenotazione() {
        return idPrenotazione;
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

    @Override
    public String stato() {
        return stato;
    }
}
