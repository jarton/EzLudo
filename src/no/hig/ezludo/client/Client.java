package no.hig.ezludo.client;

/**
 * Created by Kristian on 29.10.2015.
 */
public class Client {
    private String username;
    private char[] password;
    private String mainKey;


    public Client(String username, char[] password, String mainKey) {
        this.username = username;
        this.password = password;
        this.mainKey = mainKey;

        MainController mainController = new MainController();
        mainController.main(null);
    }

}