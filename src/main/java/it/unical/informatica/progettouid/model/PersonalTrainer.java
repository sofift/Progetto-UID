package it.unical.informatica.progettouid.model;

public class PersonalTrainer extends User{
    private String specializzazione;
    private String telefono;

    public PersonalTrainer(int id, String nome, String cognome, String dataNascita, String specializzazione, String email,  String telefono) {
        super(id, nome, cognome, dataNascita, email);
        this.specializzazione = specializzazione;
        this.telefono = telefono;
    }

    public String getSpecializzazione() {
        return specializzazione;
    }

    public String getTelefono() {
        return telefono;
    }
}


