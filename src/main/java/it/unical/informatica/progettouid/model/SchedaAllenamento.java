package it.unical.informatica.progettouid.model;

public record SchedaAllenamento(int idScheda, String dataInizio, String dataFine, String obiettivi, String notes, String suggerimentiAlimentari){
    @Override
    public int idScheda() {
        return idScheda;
    }



    @Override
    public String dataInizio() {
        return dataInizio;
    }

    @Override
    public String dataFine() {
        return dataFine;
    }

    @Override
    public String obiettivi() {
        return obiettivi;
    }

    @Override
    public String notes() {
        return notes;
    }

    @Override
    public String suggerimentiAlimentari() {
        return suggerimentiAlimentari;
    }


}
