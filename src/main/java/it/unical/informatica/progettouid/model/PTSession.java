package it.unical.informatica.progettouid.model;

public class PTSession {
    private static PTSession instance;
    private PersonalTrainer loggedTrainer;

    private PTSession(){}

    public static PTSession getInstance(){
        if(instance == null){
            instance = new PTSession();
        }
        return instance;
    }

    public void setCurrentTrainer(PersonalTrainer trainer) {
        this.loggedTrainer = trainer;
    }

    public PersonalTrainer getCurrentTrainer() {
        if (loggedTrainer == null) {
            throw new IllegalStateException("Nessun client loggato");
        }
        return loggedTrainer;
    }


    public void logout() {
        loggedTrainer = null;
    }
}
