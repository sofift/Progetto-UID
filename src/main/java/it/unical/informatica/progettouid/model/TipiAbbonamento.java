package it.unical.informatica.progettouid.model;

public class TipiAbbonamento {
    private final int id;
    private final String nome;
    private final String descrizione;
    private final int accessiTotali;
    private final int durataMesi;
    private final int prezzo;
    private final String dedicatoA;

    public TipiAbbonamento(int id, String nome, String descrizione, int accessiTotali, int durataMesi, int prezzo, String dedicatoA) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.accessiTotali = accessiTotali;
        this.durataMesi = durataMesi;
        this.prezzo = prezzo;
        this.dedicatoA = dedicatoA;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getAccessiTotali() {
        return accessiTotali;
    }

    public int getDurataMesi() {
        return durataMesi;
    }

    public int getPrezzo() {
        return prezzo;
    }

    public String getDedicatoA() {
        return dedicatoA;
    }
}
