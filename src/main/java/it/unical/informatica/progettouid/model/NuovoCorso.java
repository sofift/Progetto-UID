package it.unical.informatica.progettouid.model;
import java.time.LocalTime;
import java.util.List;

public class NuovoCorso {
    private String nome;
    private String descrizione;
    private int durata;
    private String pt;
    private List<String> giorni;
    private LocalTime orarioInizio;
    private LocalTime orarioFine;
    private int maxPartecipanti;

    // Getters and setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public int getDurata() { return durata; }
    public void setDurata(int durata) { this.durata = durata; }

    public String getPt() { return pt; }
    public void setPt(String pt) { this.pt = pt; }

    public List<String> getGiorni() { return giorni; }
    public void setGiorni(List<String> giorni) { this.giorni = giorni; }

    public LocalTime getOrarioInizio() { return orarioInizio; }
    public void setOrarioInizio(LocalTime orarioInizio) { this.orarioInizio = orarioInizio; }

    public LocalTime getOrarioFine() { return orarioFine; }
    public void setOrarioFine(LocalTime orarioFine) { this.orarioFine = orarioFine; }

    public int getMaxPartecipanti() { return maxPartecipanti; }
    public void setMaxPartecipanti(int maxPartecipanti) { this.maxPartecipanti = maxPartecipanti; }
}
