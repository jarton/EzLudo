package no.hig.ezludo.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles the GUI of the lobby, and whatever events may happen in the window.
 * Date: 30.10.2015
 * @author Per-Kristian Nilsen
 */
public class MainController extends Application {
    @FXML private TabPane tabPane;
    private HashMap<String, ChatController> tabMap = new HashMap<>();
    private HashMap<String, GameController> gameMap = new HashMap<>();
    public ListView chatListView;
    public Scene lobbyScene;
    private String[] users;
    private String[] playerNames;
    private String nickName;
    private String firstTurnCommand[] = null;
    private static Logger logger = Logger.getAnonymousLogger();
    public static Client client;

    /**
     * This method loads the lobby window.
     * @param primaryStage the JavaFX stage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        try {

            Parameters parameters = getParameters();
            List<String> rawArguments = parameters.getRaw();

            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Lobby.fxml"));
            Parent root = (Parent) loader.load();

            MainController.setClient(rawArguments);
            client.setMainController(loader.getController());

            primaryStage.setTitle("Ez-Ludo");
            primaryStage.setScene(lobbyScene = new Scene(root, 1200, 600));
            primaryStage.show();

            // Join the lobby chat
            client.joinChatRoom("lobby");

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    exit();
                }
            });

        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
    }

    /**
     * This method is called when the user presses enter in the text field. It will send the message to the client
     * object, which further sends the message to the server.
     * @param event the triggered event of pressing enter in the text field
     */
    @FXML
    public void handleTextFieldEvent(ActionEvent event){
        TextField source = (TextField) event.getSource();
            client.sendChatMessage(source.getText(), "0");
    }

    public void randomGame() {
        client.joinRandomGame();
    }

    /**
     * Displays a message in a chat room or game room, depending on the message from the server.
     * @param text an array of commands from the server
     */
    public void displayMessage(String[] text) {
        if (text[0].equals("CHAT")) {
            tabMap.get(text[1]).displayMessage(text[2] + ": " + text[3]);
        } else if (text[0].equals("GAME")) {
            if (text[4].equals("WIN")) {
                gameMap.get(text[1]).displayMessage(text[5] + " "+Login.messages.getString("winner"));
            }
            gameMap.get(text[1]).displayMessage(text[4] + ": " + text[5]);
        }
    }

    public void chooseChatRoomName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(Login.messages.getString("newRoom"));
        dialog.setHeaderText(Login.messages.getString("createRoom"));
        dialog.setContentText(Login.messages.getString("enterRoomName"));

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(client::joinChatRoom);
    }

    public void chooseGameRoomName() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(Login.messages.getString("newRoom"));
        dialog.setHeaderText(Login.messages.getString("createRoom"));
        dialog.setContentText(Login.messages.getString("enterRoomName"));

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(client::sendNewGameRequest);
    }

    public void newChatRoom(String[] response) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Tab tab = new Tab(response[1]);
                tabPane.getTabs().add(tab);
                try {
                    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Chatroom.fxml"));
                    tab.setContent((Node) loader.load());
                    ChatController chatController = loader.getController();
                    tabMap.put(response[1], chatController);
                    chatController.setRoomName(response[1]);
                    if (users != null && users.length > 0) {
                        tabMap.get(users[1]).updateUsers(users);
                    }
                    tab.setOnClosed(new EventHandler<Event>() {
                        @Override
                        public void handle(Event event) {
                            client.leaveChatRoom(chatController.getRoomName());
                        }
                    });
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "an exception was thrown", e);
                }
            }
        });
    }

    /**
     * This method is called from the listener when a game invitation is received. A pop up dialog will let the user
     * accept or decline the invitation. The response is sent to the server, and the process of joining the game will
     * happen automatically as the server will respond with a "GAME JOINED" message.
     * @param command the initial command array received from the server
     */
    public void respondToInvitation(String[] command) {
        String hostingPlayer = command[2];
        String gameId = command [1];

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(Login.messages.getString("invitation"));
                alert.setHeaderText(Login.messages.getString("joineGame"));
                alert.setContentText(hostingPlayer + "\n"+Login.messages.getString("inviteJoine"));

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    client.sendInvitationResponse("ACCEPT", gameId);
                } else {
                    client.sendInvitationResponse("DECLINE", gameId);
                }
            }
        });
    }

    public void updateUsers(String[] command) {
        if (tabMap.get(command[1]) == null) {
            users = command;
        }
        else
            tabMap.get(command[1]).updateUsers(command);
    }

    public void exit() {
        client.logout();
        client.closeConnection();
        client.listnerThread.interrupt();
        System.exit(0); //NOSONAR
    }

    public void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Login.messages.getString("about"));
        alert.setHeaderText(null);
        alert.setContentText("Jardar, Kristian, Per-Kristian");
        alert.showAndWait();
    }

    public void newGame(String response[]) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Tab tab = new Tab(response[2]);
                tabPane.getTabs().add(tab);
                try {
                    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Game.fxml"));
                    tab.setContent((Node) loader.load());
                    GameController gameController = loader.getController();
                    gameMap.put(response[1], gameController);
                    gameController.setGameId(response[1]);
                    gameController.setGameName(response[2]);
                    gameController.ludoBoard();

                    if (playerNames != null) {
                        gameController.setupPlayers(playerNames, nickName);
                    }

                    if (firstTurnCommand != null) {
                        gameController.playerTurn(firstTurnCommand);
                        firstTurnCommand = null;
                    }

                    tab.setOnClosed(new EventHandler<Event>() {
                        @Override
                        public void handle(Event event) {
                            client.leaveGameRoom(gameController.gameId, gameController.gameName);
                        }
                    });
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "an exception was thrown", e);
                }
            }
        });
    }

    public void updatePlayers(String[] command) {
        String gameId = command[1];

        String[] players = new String[4];
        for(int i = 3; i<command.length; i++) {
            players[i - 3] = command[i];
        }
        if (gameMap.containsKey(gameId)) {
            GameController gameController = gameMap.get(gameId);

            gameController.setupPlayers(players, nickName);
        }
        else {
            playerNames = players;
        }
    }

    public void playerRoll(String command[]) {
        gameMap.get(command[1]).playerRoll(command);
    }

    public void playerTurn(String command[]) {
        if (!gameMap.containsKey(command[1])) {
            firstTurnCommand = command;
        }
        else
            gameMap.get(command[1]).playerTurn(command);
    }

    public void playerMove(String command[]) {
        gameMap.get(command[1]).playerMove(command);
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public static void setClient(List<String> args) {
        //client = new Client(args.get(0), args.get(1), args.get(2));
        client = new Client(args.get(2));
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void startScene(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            logger.log(Level.SEVERE, "an exception was thrown", e);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "an exception was thrown", e);
        } catch (InstantiationException e) {
            logger.log(Level.SEVERE, "an exception was thrown", e);
        } catch (IllegalAccessException e) {
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
        launch(args);
    }
}
