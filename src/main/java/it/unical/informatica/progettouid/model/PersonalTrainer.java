package it.unical.informatica.progettouid.model;

public record PersonalTrainer(int id, String nome, String cognome, String dataNascita, String specializzazione, String email, String password, String telefono) {

    public PersonalTrainer {
        if (password == null) {
            password = "";  // Empty string for when password is not needed
        }
    }

    // Omit password from toString for security
    @Override
    public String toString() {
        return "PersonalTrainer[id=" + id +
                ", nome=" + nome +
                ", cognome=" + cognome +
                ", dataNascita=" + dataNascita +
                ", specializzazione=" + specializzazione +
                ", email=" + email +
                ", telefono=" + telefono + "]";
    }

    // Factory method for creating trainer without password
    public static PersonalTrainer createWithoutPassword(
            int id, String nome, String cognome, String dataNascita,
            String specializzazione, String email, String telefono) {
        return new PersonalTrainer(id, nome, cognome, dataNascita,
                specializzazione, email, "", telefono);
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public String nome() {
        return nome;
    }

    @Override
    public String cognome() {
        return cognome;
    }

    @Override
    public String dataNascita() {
        return dataNascita;
    }

    @Override
    public String specializzazione() {
        return specializzazione;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String telefono() {
        return telefono;
    }
}


