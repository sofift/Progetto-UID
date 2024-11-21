package it.unical.informatica.progettouid.model;

public class PersonalTrainer {
    protected int id;
    protected String nome;
    protected String cognome;
    protected String dataDiNascita;
    protected String specializzazione;

    public PersonalTrainer(int id, String name, String surname, String dateborn, String specializzazione) {
        this.id = id;
        this.nome = name;
        this.cognome = surname;
        this.dataDiNascita = dateborn;
        this.specializzazione = specializzazione;
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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getDataDiNascita() {
        return dataDiNascita;
    }

    public void setDataDiNascita(String dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public String getSpecializzazione() {
        return specializzazione;
    }

    public void setSpecializzazione(String specializzazione) {
        this.specializzazione = specializzazione;
    }
}
