package it.unical.informatica.progettouid.model;

public record OrariCorsi(String nomeCorso, String giorno, String oraInizio, String oraFine) {
    @Override
    public String nomeCorso() {
        return nomeCorso;
    }

    @Override
    public String giorno() {
        return giorno;
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
