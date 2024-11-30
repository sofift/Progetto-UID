package it.unical.informatica.progettouid.model;

public record PersonalTrainer(int id, String name, String surname, String dateborn, String specializzazione) {
    @Override
    public int id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String surname() {
        return surname;
    }

    @Override
    public String dateborn() {
        return dateborn;
    }

    @Override
    public String specializzazione() {
        return specializzazione;
    }
}
