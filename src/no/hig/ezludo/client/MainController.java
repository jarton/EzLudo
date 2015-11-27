package no.hig.ezludo.client;

import Internationalization.Internationalization;
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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is handles the GUI of the main window. Any changes to the functionality of this GUI should happen here.
 * The controller is attached to Lobby.fxml. The main window has tabs of games and chat rooms. The chat rooms are
 * contained in a HashMap called tabMap. The game rooms are contained in another HashMap called gameMap. This class was
 * originally meant to be a controller for the "lobby" chat, but was reworked to be the controller of the entire main
 * window. Some variable names may seem odd because of this.
 * @author Per-Kristian Nilsen
 * @since 30.10.2015
 */
public class MainController extends Application {
    @FXML private TabPane tabPane;
    private HashMap<String, ChatController> tabMap = new HashMap<>();
    private HashMap<String, GameController> gameMap = new HashMap<>();
    private String[] users;
    private String[] playerNames;
    private String nickName;
    private String firstTurnCommand[] = null;
    private static Logger logger = Logger.getAnonymousLogger();
    private static Client client;
    @FXML Menu file;
    @FXML Menu edit;
    @FXML Menu help;
    @FXML MenuItem about;
    @FXML MenuItem randomGame;
    @FXML MenuItem newGame;
    @FXML MenuItem newChat;
    @FXML MenuItem exit;
    @FXML Button ranGame;
    @FXML Button createChat;
    @FXML Button createGame;
    @FXML Tab welcome;
    @FXML Label welcomeText;


    /**
     * This method loads the lobby GUI from the Lobby.fxml file. It automatically joins the lobby chat.
     * @param primaryStage the JavaFX stage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Parameters parameters = getParameters();
            List<String> rawArguments = parameters.getRaw();

            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Lobby.fxml"));
            Parent root = (Parent) loader.load();

            // Creates a client object, and gives it a reference to this controller.
            MainController.setClient(rawArguments);
            client.setMainController(loader.getController());

            primaryStage.setTitle("Ez-Ludo");
            primaryStage.setScene(new Scene(root, 1200, 600));
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
     * Translate all File, FileItems, Buttons and Labels
     * */
    public void setI18N () {
        file.setText(Login.getTranslation().getString("file"));
        exit.setText(Login.getTranslation().getString("exit"));
        edit.setText(Login.getTranslation().getString("edit"));
        newChat.setText(Login.getTranslation().getString("newChat"));
        newGame.setText(Login.getTranslation().getString("newGame"));
        randomGame.setText(Login.getTranslation().getString("randomGame"));
        about.setText(Login.getTranslation().getString("about"));
        help.setText(Login.getTranslation().getString("help"));
        ranGame.setText(Login.getTranslation().getString("randomGame"));
        createChat.setText(Login.getTranslation().getString("newChat"));
        createGame.setText(Login.getTranslation().getString("newGame"));
        welcome.setText(Login.getTranslation().getString("welcome"));
        welcomeText.setText(Login.getTranslation().getString("welcome"));
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

    /**
     * This method is called when the user wants to join a random game. It tells the client to send a random game
     * request to the server, and when the game is filled up, it will automatically pop up as a tab.
     */
    public void randomGame() {
        client.joinRandomGame();
    }

    /**
     * Displays a message in a chat room or game room, depending on the message from the server. It does this by
     * passing the message on to the correct game- or chatController.
     * @param text an array of commands from the server
     */
    public void displayMessage(String[] text) {
        if (text[0].equals("CHAT")) {
            // Run the correct chatController's displayMessage with the parameter as "Name: message"
            tabMap.get(text[1]).displayMessage(text[2] + ": " + text[3]);
        } else if (text[0].equals("GAME")) {
            if (text[4].equals("WIN")) {
                // Writes who won the game to the game chat
                gameMap.get(text[1]).displayMessage(text[5] + " " + Login.getTranslation().getString("winner"));
            } else {
                // Run the correct gameController's displayMessage with the parameter as "Name: message"
                gameMap.get(text[1]).displayMessage(text[4] + ": " + text[5]);
            }
        }
    }

    /**
     * This method shows a TextInputDialog, telling the user to enter a chat room to create/join. When they have
     * specified a name, they will either create the room, or join the existing room with that name.
     */
    public void chooseChatRoomName() {
        // Show the dialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(Login.getTranslation().getString("newRoom"));
        dialog.setHeaderText(Login.getTranslation().getString("createRoom"));
        dialog.setContentText(Login.getTranslation().getString("enterRoomName"));

        Optional<String> result = dialog.showAndWait();

        // Get the room name and send it to client.joinChatRoom.
        result.ifPresent(client::joinChatRoom);
    }

    /**
     * This method shows a TextInputDialog, telling the user to enter a game room to create/join. When they have
     * specified a name, they will either create the room, or join the existing room with that name.
     */
    public void chooseGameRoomName() {
        // Show the dialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(Login.getTranslation().getString("newRoom"));
        dialog.setHeaderText(Login.getTranslation().getString("createRoom"));
        dialog.setContentText(Login.getTranslation().getString("enterRoomName"));

        Optional<String> result = dialog.showAndWait();
        // Get the room name and send it to client.sendNewGameRequest.
        result.ifPresent(client::sendNewGameRequest);
    }

    /**
     * This method is for creating a new tab for a new chat room. It creates the tab with the room name as title, and
     * loads the correct GUI from the Chatroom.fxml file. This method is called when the listener receives a message
     * from the server saying that the user joined a chat room.
     * @param response the message from the server
     */
    public void newChatRoom(String[] response) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Create the tab with the correct title
                Tab tab = new Tab(response[1]);
                tabPane.getTabs().add(tab);
                try {
                    // Load the GUI of the chat room
                    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Chatroom.fxml"));
                    tab.setContent((Node) loader.load());
                    // Get a chatController and store it as a value in the tabMap HashMap. The room name is the key.
                    ChatController chatController = loader.getController();
                    tabMap.put(response[1], chatController);
                    chatController.setRoomName(response[1]);

                    if (users != null && users.length > 0) {
                        // Update user list
                        tabMap.get(users[1]).updateUsers(users);
                    }
                    tab.setOnClosed(new EventHandler<Event>() {
                        @Override
                        public void handle(Event event) {
                            // Tell the server that the user has left if they close the tab
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
                // Display the pop up message
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(Login.getTranslation().getString("invitation"));
                alert.setHeaderText(Login.getTranslation().getString("joineGame"));
                alert.setContentText(hostingPlayer + "\n" + Login.getTranslation().getString("inviteJoin"));

                Optional<ButtonType> result = alert.showAndWait();
                // Send a response to the server
                if (result.get() == ButtonType.OK){
                    client.sendInvitationResponse("ACCEPT", gameId);
                } else {
                    client.sendInvitationResponse("DECLINE", gameId);
                }
            }
        });
    }

    /**
     * This method stores users. If the correct chat room isn't loaded, store the users in the main controller. If it
     * finds the right chat room, it will update it's users.
     * @param command the message from the server
     */
    public void updateUsers(String[] command) {
        if (tabMap.get(command[1]) == null) {
            users = command;
        }
        else
            tabMap.get(command[1]).updateUsers(command);
    }

    /**
     * This method logs out the client, closes the connection, stops the listener and quits the application.
     */
    public void exit() {
        client.logout();
        client.closeConnection();
        client.getListenerThread().interrupt();
        System.exit(0); //NOSONAR
    }

    /**
     * Displays a simple message with the developers' names.
     */
    public void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Login.getTranslation().getString("about"));
        alert.setHeaderText(null);
        alert.setContentText("Jardar, Kristian, Per-Kristian");
        alert.showAndWait();
    }

    /**
     * This method is for creating a new tab for a new game room. It creates the tab with the room name as title, and
     * loads the correct GUI from the Game.fxml file. This method is called when the listener receives a message
     * from the server saying that the user joined a game.
     * @param response the message from the server
     */
    public void newGame(String response[]) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Create a new tab with correct title
                Tab tab = new Tab(response[2]);
                tabPane.getTabs().add(tab);
                try {
                    // Load GUI from fxml
                    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Game.fxml"));
                    tab.setContent((Node) loader.load());
                    GameController gameController = loader.getController();
                    // Put the game in the HashMap
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
                            // Leave the game when user closes the tab
                            client.leaveGameRoom(gameController.gameId, gameController.gameName);
                        }
                    });
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "an exception was thrown", e);
                }
            }
        });
    }

    /**
     * This method saves players. If it can't find the correct gameController, it will store the players in the main
     * controller. If it can find it, it will update the players internally in the game controller.
     * @param command the message from the server
     */
    public void updatePlayers(String[] command) {
        String gameId = command[1];

        String[] players = new String[4];
        // fill the players array with the player names
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

    /**
     * Updates a game with who rolled the dice, and what they got.
     * @param command the message from the server
     */
    public void playerRoll(String command[]) {
        gameMap.get(command[1]).playerRoll(command);
    }

    /**
     * Updates a game with who's turn it is.
     * @param command the message from the server
     */
    public void playerTurn(String command[]) {
        if (!gameMap.containsKey(command[1])) {
            firstTurnCommand = command;
        }
        else
            gameMap.get(command[1]).playerTurn(command);
    }

    /**
     * Updates a game with who moved a piece and where.
     * @param command
     */
    public void playerMove(String command[]) {
        gameMap.get(command[1]).playerMove(command);
    }

    /**
     * Standard "setter" for nicname.
     * @param nickName the nick name
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Creates a new client object.
     * @param args the key for connecting to the main port
     */
    public static void setClient(List<String> args) {
        client = new Client(args.get(2));
    }

    /**
     * Standard "getter" for the client object reference
     * @return a reference to the client object
     */
    public static Client getClient() {
        return client;
    }

    /**
     * Launches the JavaFX stage.
     * @param args standard main args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts a scene with the look and feel of the user's operating system.
     * @param args array of arguments
     */
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
