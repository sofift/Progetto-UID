package it.unical.informatica.progettouid.model;

public class Abbonamento {
    private final int accessiRimanenti;
    private final int accessiTotali;
    private final String dataScadenza;
    private final String tipoAbbonamento;

    public Abbonamento(int accessiRimanenti, int accessiTotali, String dataScadenza, String tipoAbbonamento) {
        this.accessiRimanenti = accessiRimanenti;
        this.accessiTotali = accessiTotali;
        this.dataScadenza = dataScadenza;
        this.tipoAbbonamento = tipoAbbonamento;
    }

    public int getAccessiRimanenti() { return accessiRimanenti; }
    public int getAccessiTotali() { return accessiTotali; }
    public String getDataScadenza() { return dataScadenza; }
    public String getTipoAbbonamento() { return tipoAbbonamento; }
}
