package no.hig.ezludo.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import sun.applet.Main;

import java.io.*;
import java.net.Socket;

/**
 * Created by Kristian on 29.10.2015.
 */
public class Client {
    private String email;
    private String password;
    private String mainKey;
    private Socket socket;
    private String serverIP = "128.39.142.181";
    private int portNumber = 9696;
    private PrintWriter output;
    private BufferedReader input;
    private MainController mainController;


    public Client(String email, String password, String mainKey) {
        this.email = email;
        this.password = password;
        this.mainKey = mainKey;
        setUpConnection();
        connectToLobby();
    }

    public void sendChatMessage(String message, String chatRoomId) {
        output.println("CHAT|" + chatRoomId + "|LOBBY|" + email + "|" + message);
        output.flush();
    }

    public void setMainController(MainController ctrl) {
        this.mainController = ctrl;
    }

    /**
     * This function starts a new thread for listening for messages from the server. It is called when the server allows
     * the client to use the main socket.
     */
    public void startListener() {
        new Thread (()->{
            while (input!=null) {
                String cmd;
                try {
                    while ((cmd=input.readLine())!=null) {
                        String command[] = cmd.split("\\|");
                        if (command[0].equals("CHAT")) {
                                System.out.println("recived chat");
                                mainController.displayMessage(command);
                            /*
                        } else if (command[0].equals("NEW GAME")) {
                            startNewGame (command);
                        } else if (command[0].equals("PLAYER TURN")) {
                            games.get(Long.parseLong(command[1])).showActivePlayer(Integer.parseInt(command[2]));
                            if (command.length>3&&command[3].equals("YOUR TURN"))
                                games.get(Long.parseLong(command[1])).myTurn(Integer.parseInt(command[2]));
                        } else if (command[0].equals("MOVE")) {
                            games.get(Long.parseLong(command[1])).movePiece(Integer.parseInt(command[2]), Integer.parseInt(command[3]), Integer.parseInt(command[4]));
                       */ }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        // TODO: Discuss ways to handle chat. Need to decide on structure of client program in order to finish the method.
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
                startListener();
                System.out.println("listener started");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}