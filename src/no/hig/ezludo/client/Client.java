package no.hig.ezludo.client;

/**
 * Created by Kristian on 29.10.2015.
 */
public class Client {
    private String username;
    private char[] password;


    public Client(String username, char[] password) {
        this.username = username;
        this.password = password;
        MainController mainController = new MainController();
        mainController.main(null);
    }

}