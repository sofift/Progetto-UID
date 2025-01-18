package it.unical.informatica.progettouid.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Esercizio {
    private final SimpleIntegerProperty nSerie;
    private final SimpleIntegerProperty nRipetizioni;
    private final SimpleIntegerProperty tmpRecupero;
    private final SimpleStringProperty notes;
    private final SimpleStringProperty nomeEserc;
    private final SimpleStringProperty gMuscolare;

    public Esercizio(String nomeEserc, int nSerie, int nRipetizioni, int tmpRecupero, String gMuscolare, String notes) {
        this.nSerie = new SimpleIntegerProperty(nSerie);
        this.nRipetizioni = new SimpleIntegerProperty(nRipetizioni);
        this.tmpRecupero = new SimpleIntegerProperty(tmpRecupero);
        this.notes = new SimpleStringProperty(notes);
        this.nomeEserc = new SimpleStringProperty(nomeEserc);
        this.gMuscolare = new SimpleStringProperty(gMuscolare);
    }

    public int getnSerie() {
        return nSerie.get();
    }

    public SimpleIntegerProperty nSerieProperty() {
        return nSerie;
    }

    public int getnRipetizioni() {
        return nRipetizioni.get();
    }

    public SimpleIntegerProperty nRipetizioniProperty() {
        return nRipetizioni;
    }

    public int getTmpRecupero() {
        return tmpRecupero.get();
    }

    public SimpleIntegerProperty tmpRecuperoProperty() {
        return tmpRecupero;
    }

    public String getNotes() {
        return notes.get();
    }

    public SimpleStringProperty notesProperty() {
        return notes;
    }

    public String getNomeEserc() {
        return nomeEserc.get();
    }

    public SimpleStringProperty nomeEsercProperty() {
        return nomeEserc;
    }


    public String getgMuscolare() {
        return gMuscolare.get();
    }

    public SimpleStringProperty gMuscolareProperty() {
        return gMuscolare;
    }




}
