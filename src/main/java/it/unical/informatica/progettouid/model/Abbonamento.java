package it.unical.informatica.progettouid.model;

public record Abbonamento(int accessiRimanenti, int accessiTotali, String dataScadenza, String tipoAbbonamento) {
    @Override
    public int accessiRimanenti() {
        return accessiRimanenti;
    }

    @Override
    public int accessiTotali() {
        return accessiTotali;
    }

    @Override
    public String dataScadenza() {
        return dataScadenza;
    }

    @Override
    public String tipoAbbonamento() {
        return tipoAbbonamento;
    }
}
