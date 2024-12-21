package it.unical.informatica.progettouid.model;

public record EsercizioScheda(int nSerie, int nRipetizioni, int tmpRecupero, String notes, String nomeEserc,  String gMuscolare, String giorno) {

    @Override
    public int nSerie() {
        return nSerie;
    }

    @Override
    public int nRipetizioni() {
        return nRipetizioni;
    }

    @Override
    public int tmpRecupero() {
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
