package it.unical.informatica.progettouid.model;

public class Client extends User{
    private String abbonamento;

    public Client(int id, String nome, String cognome, String dataNascita, String abbonamento, String email) {
        super(id, nome, cognome, dataNascita, email);
        this.abbonamento = abbonamento;
    }

    public String getAbbonamento() {
        return abbonamento;
    }

}
