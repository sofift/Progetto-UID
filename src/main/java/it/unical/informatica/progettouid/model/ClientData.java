package it.unical.informatica.progettouid.model;


// da rivedere

public record ClientData(int id, String email, String nome, String cognome, String abbonamento, String dataNascita) {

    public static ClientData fromUsersAndClient(Users user, Client client){
        return new ClientData(
                client.id(),
                user.getEmail(),
                client.nome(),
                client.cognome(),
                client.abbonamento(),
                client.dataNascita()
        );
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public String email() {
        return email;
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
    public String abbonamento() {
        return abbonamento;
    }

    @Override
    public String dataNascita() {
        return dataNascita;
    }
}
