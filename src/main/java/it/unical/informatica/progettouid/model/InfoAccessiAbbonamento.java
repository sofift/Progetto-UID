package it.unical.informatica.progettouid.model;

public record InfoAccessiAbbonamento(int accessiRimanenti, int accessiTotali, String dataScadenza, String tipoAbbonamento) {
    @Override
    public int accessiRimanenti() {
        return accessiRimanenti;
    }

    @Override
    public int accessiTotali() {
        return accessiTotali;
    }

    @Override
    public String tipoAbbonamento() {
        return tipoAbbonamento;
    }

    @Override
    public String dataScadenza() {
        return dataScadenza;
    }
}
