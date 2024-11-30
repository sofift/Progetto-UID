package it.unical.informatica.progettouid.model;

public class Users{
    protected int id;
    protected String email;
    protected String password;

    public  Users(int id, String email, String password){
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
