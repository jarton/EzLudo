package no.hig.ezludo.client;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Per-Kristian, Kristian
 * @since 29.10.2015
 */
public class Client {
    private String nickName;
    private String mainKey;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private MainController mainController;
    private static Logger logger;
    public Thread listnerThread;


    /**
     * This constructor defines necessary private variables, sets up a connection and connects to the lobby.
     * @param mainKey Main Key for connecting to socket
     */
    public Client(String mainKey) {
        logger = Logger.getAnonymousLogger();
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

    public void leaveGameRoom(String gameId, String roomName) {
        output.println("GAME|" + gameId + "|" + roomName + "|LEAVE");
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
        listnerThread = new Thread(() -> {
            while (input != null) {
                String cmd;
                try {
                    while (mainController != null && (cmd = input.readLine()) != null) {
                        String command[] = cmd.split("\\|");
                        if (("CHAT").equals(command[0])) {
                            mainController.displayMessage(command);
                        } else if (("CHAT JOINED").equals(command[0])) {
                            mainController.newChatRoom(command);
                        } else if (("USERS").equals(command[0])) {
                            mainController.updateUsers(command);
                        } else if (("GAME JOINED").equals(command[0])) {
                            mainController.newGame(command);
                        } else if (("GAME INVITE").equals(command[0])) {
                            mainController.respondToInvitation(command);
                        } else if (("GAME USERS").equals(command[0])) {
                            mainController.updatePlayers(command);
                        } else if (("GAME").equals(command[0])) {
                            if (("TURN").equals(command[3])) {
                                mainController.playerTurn(command);
                            } else if (("ROLL").equals(command[3])) {
                                mainController.playerRoll(command);
                            } else if (("MOVE").equals(command[3])) {
                                mainController.playerMove(command);
                            } else mainController.displayMessage(command);
                        }
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "an exception was thrown", e);
                }
            }
        });
        listnerThread.start();
    }

    /**
     * Sends a response to a received game invitation. The response parameter should either be "ACCEPT" or "DECLINE".
     * @param response "ACCEPT" or "DECLINE"
     * @param gameId the id of the game
     */
    public void sendInvitationResponse(String response, String gameId) {
        output.println("GAME INVITE|" + gameId + "|" + response);
        output.flush();
    }

    public void sendGameInvite(String name, String gameId) {
        output.println("GAME INVITE|" + gameId + "|" + name);
        output.flush();
    }

    /**
     * This method sends a "new game" request to the server. It is called from MainController::chooseGameRoomName.
     * @param roomName the name of the game room
     */
    public void sendNewGameRequest(String roomName) {
        output.println("CREATE GAME|" + roomName);
        output.flush();
    }

    /**
     * This method tells the server to start the game with the specified id from the method parameter.
     * @param gameId the game id
     */
    public void startGame(String gameId) {
        output.println("GAME START|" + gameId);
        output.flush();
    }

    /**
     * This method sets up the socket, and the input- and output streams for that socket. If there's already a
     * connection, it will close the existing socket, output and input first.
     */
    public void setUpConnection() {
        try {
            closeConnection();
            socket = new Socket(Constants.getServerIP(), Constants.getPortNumber());
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "an exception was thrown", e);
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
            logger.log(Level.SEVERE, "an exception was thrown", e);
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
            logger.log(Level.SEVERE, "an exception was thrown", e);
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