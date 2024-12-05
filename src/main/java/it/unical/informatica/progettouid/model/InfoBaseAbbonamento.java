package it.unical.informatica.progettouid.model;

public record InfoBaseAbbonamento(String nomeAbbonamento, String dataInizio, String dataScadenza, String stato, String descrizione) {
    @Override
    public String nomeAbbonamento() {
        return nomeAbbonamento;
    }

    @Override
    public String dataInizio() {
        return dataInizio;
    }

    @Override
    public String dataScadenza() {
        return dataScadenza;
    }

    @Override
    public String stato() {
        return stato;
    }
}
