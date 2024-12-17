package it.unical.informatica.progettouid.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AdminPasswordGenerator {

    public static String generateHashedPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
    public static void main(String[] args) {
        String adminPassword1 = "adminPassword1";
        String adminPassword2 = "adminPassword2";

        String hashedPassword1 = generateHashedPassword(adminPassword1);
        String hashedPassword2 = generateHashedPassword(adminPassword2);

        System.out.println("Admin 1 Password Hash: " + hashedPassword1);
        System.out.println("Admin 2 Password Hash: " + hashedPassword2);
    }
}
