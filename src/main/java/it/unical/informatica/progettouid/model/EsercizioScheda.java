package it.unical.informatica.progettouid.model;

public record EsercizioScheda(String nSerie, String nRipetizioni, String tmpRecupero, String notes, String nomeEserc,  String gMuscolare, String giorno) {

    @Override
    public String nSerie() {
        return nSerie;
    }

    @Override
    public String nRipetizioni() {
        return nRipetizioni;
    }

    @Override
    public String tmpRecupero() {
        return tmpRecupero;
    }

    @Override
    public String notes() {
        return notes;
    }

    @Override
    public String nomeEserc() {
        return nomeEserc;
    }


    @Override
    public String gMuscolare() {
        return gMuscolare;
    }

}
