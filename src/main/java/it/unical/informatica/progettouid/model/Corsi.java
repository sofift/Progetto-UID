package it.unical.informatica.progettouid.model;

public class Corsi {
    protected int id;
    protected String nome;
    protected String tipo;
    protected int maxPartecipanti;
    protected int durata;
    protected String PT;
    protected String oraInizio;
    protected String oraFine;

    public Corsi(int id, String nome, String tipo, int maxPartecipanti, int durata, String PT, String oraInizio, String oraFine) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.maxPartecipanti = maxPartecipanti;
        this.durata = durata;
        this.PT = PT;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
    }

    public String getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(String oraInizio) {
        this.oraInizio = oraInizio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getMaxPartecipanti() {
        return maxPartecipanti;
    }

    public void setMaxPartecipanti(int maxPartecipanti) {
        this.maxPartecipanti = maxPartecipanti;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public String getPT() {
        return PT;
    }

    public void setPT(String PT) {
        this.PT = PT;
    }
}
