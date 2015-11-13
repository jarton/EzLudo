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
    private String nickName;
    private String password;
    private String mainKey;
    private Socket socket;
    private String serverIP = Constants.serverIP;
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

    public void sendChatMessage(String message, String chatRoomName) {
        output.println("CHAT|" + chatRoomName + "|" + nickName + "|" + message);
        output.flush();
    }

    public void joinChatRoom(String roomName) {
        output.println("JOIN CHAT|" + roomName);
        output.flush();
    }

    public void joinRandomGame() {
        output.println("JOIN RANDOM");
        output.flush();
    }

    public void rollDice(String gameId, String gameName) {
        //output.println("GAME|" + id + "|" + gameName + "|ROLL");
        //TODO finsh this
        output.flush();
    }

    public void movePiece(String gameId, String gameName, String pieceToMove) {
        //output.println("GAME|" + id + "|" + gameName + "|MOVE|"+ pieceToMove);
        //TODO finsh this
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
                        } else if (command[0].equals("CHAT JOINED")) {
                            mainController.newChatRoom(command);
                        } else if (command[0].equals("USERS")) {
                            mainController.updateUsers(command);
                        } else if (command[0].equals("GAME STARTED")) {
                            mainController.startGame(command);
                        } else if (command[0].equals("GAME")) {
                            //TODO HANDLE GAME COMMANDS
                       }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
            String response = input.readLine();
            if (response.startsWith("LOGGED IN")) {
                String[] command = response.split("\\|");
                nickName = command[1];
                startListener();
                System.out.println("listener started");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}