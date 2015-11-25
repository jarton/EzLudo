package no.hig.ezludo.client;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kristian on 29.10.2015.
 */
public class Client {
    private Constants constants;
    private String email;
    private String nickName;
    private String password;
    private String mainKey;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private MainController mainController;
    private static Logger logger = Logger.getAnonymousLogger();


    /**
     * This constructor defines necessary private variables, sets up a connection and connects to the lobby.
     * @param email The username
     * @param password The password
     * @param mainKey Main Key for connecting to socket
     */
    public Client(String email, String password, String mainKey) {
        this.email = email;
        this.password = password;
        this.mainKey = mainKey;
        setUpConnection();
        connectToLobby();
    }

    /**
     * This method sends a chat message to the server.
     * @param message the message
     * @param chatRoomName the chat room
     */
    public void sendChatMessage(String message, String chatRoomName) {
        output.println("CHAT|" + chatRoomName + "|" + nickName + "|" + message);
        output.flush();
    }

    /**
     * This method sends a game chat message to the server.
     * @param message the message
     * @param id the id of the game
     * @param gameRoomName the game room name
     */
    public void sendGameMessage(String message, String id, String gameRoomName) {
        output.println("GAME|" + id + "|" + gameRoomName + "|CHAT|" + nickName + "|" + message);
        output.flush();
    }

    /**
     * This method tells the server to let the user join a chat room.
     * @param roomName the chat room
     */
    public void joinChatRoom(String roomName) {
        output.println("JOIN CHAT|" + roomName);
        output.flush();
    }

    public void joinGameRoom(String roomName) {
        output.println("JOIN GAME|" + roomName);
        output.flush();
    }

    public void joinRandomGame() {
        output.println("JOIN RANDOM");
        output.flush();
    }

    public void rollDice(String gameId, String gameName) {
        output.println("GAME|" + gameId + "|" + gameName + "|ROLL");
        output.flush();
    }

    public void movePiece(String gameId, String gameName, String pieceToMove) {
        output.println("GAME|" + gameId + "|" + gameName + "|MOVE|"+ pieceToMove);
        output.flush();
    }

    /**
     * This method handles leaving a chat room. The chatroom name is specified in the parameter.
     * @param roomName the name of the chat room
     */
    public void leaveChatRoom(String roomName) {
        output.println("LEAVE CHAT|" + roomName);
        output.flush();
    }

    /**
     * This method saves a reference to the main controller.
     * @param ctrl the main controller
     */
    public void setMainController(MainController ctrl) {
        this.mainController = ctrl;
        this.mainController.setNickName(nickName);
    }

    /**
     * This function starts a new thread for listening for messages from the server. It is called when the server allows
     * the client to use the main socket.
     */
    public void startListener() {
        new Thread(() -> {
            while (input != null) {
                String cmd;
                try {
                    while (mainController != null && (cmd = input.readLine()) != null) {
                        String command[] = cmd.split("\\|");
                        if (command[0].equals("CHAT")) {
                            mainController.displayMessage(command);
                        } else if (command[0].equals("CHAT JOINED")) {
                            mainController.newChatRoom(command);
                        } else if (command[0].equals("USERS")) {
                            mainController.updateUsers(command);
                        } else if (command[0].equals("GAME STARTED")) {
                            mainController.newGame(command);
                        } else if (command[0].equals("GAME")) {
                            if (command[3].equals("TURN")) {
                                mainController.playerTurn(command);
                            } else if (command[3].equals("ROLL")) {
                                mainController.playerRoll(command);
                            } else if (command[3].equals("MOVE")) {
                                mainController.playerMove(command);
                            } else mainController.displayMessage(command);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "an exception was thrown", e);
                }
            }
        }).start();
    }

    /**
     * This method sets up the socket, and the input- and output streams for that socket. If there's already a
     * connection, it will close the existing socket, output and input first.
     */
    public void setUpConnection() {
        try {
            closeConnection();
            socket = new Socket(constants.getServerIP(), constants.getPortNumber());
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method closes the socket, input and output.
     */
    public void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
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
            System.out.println(response);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method sends a logout message to the server, and closes the connection.
     */
    public void logout() {
        output.println("LOGOUT");
        closeConnection();
    }

    /**
     * Returns a reference to the PrintWriter of the client.
     * @return the print writer
     */
    public PrintWriter getOutput() {
        return output;
    }

    /**
     * Returns a reference to the BufferedReader of the client.
     * @return the BufferedReader
     */
    public BufferedReader getInput() {
        return input;
    }
}