package no.hig.ezludo.client;

import javax.swing.*;

/**
 * Created by Kristian on 29.10.2015.
 */
public class Client {
    String username;
    char[] password;


    public Client(String username, char[] password) {
        this.username = username;
        this.password = password;
        System.out.print(username);
        System.out.print(password[0]);
    }

}