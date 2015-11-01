package no.hig.ezludo.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by Kristian on 29.10.2015.
 */
public class Client {
    private String username;
    private char[] password;
    private String mainKey;
    private Socket socket;
    private String serverIP = "127.0.0.1";
    private int portNumber = 9696;
    private PrintWriter output;
    private BufferedReader input;
    private MainController mainController;


    public Client(String username, char[] password, String mainKey) {
        this.username = username;
        this.password = password;
        this.mainKey = mainKey;
        mainController = new MainController(this);
        setUpConnection();
        connectToLobby();

    }

    public void sendChatMessage(String message) {
        output.println(message);
        output.flush();
    }

    /**
     * This method sets up the socket, and the input- and output streams for that socket.
     */
    public void setUpConnection() {
        try {
            socket = new Socket(serverIP, 9696);
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sends the key to the server, and depending on whether the key is correct or not, the server will
     * allow the client to chat, or deny it.
     */
    public void connectToLobby() {
        try {
            output.println(mainKey);
            output.flush();
            if (input.readLine().equals("LOGGED IN")) {
                mainController.displayLobbyMessage("Joined Lobby.");
            } else {
                mainController.displayLobbyMessage("Illegal connection.");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}