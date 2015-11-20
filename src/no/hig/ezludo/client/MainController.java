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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
            MainController.setClient(rawArguments);

            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Lobby.fxml"));
            Parent root = (Parent) loader.load();

            client.setMainController(loader.getController());

            primaryStage.setTitle("Ez-Ludo");
            primaryStage.setScene(lobbyScene = new Scene(root, 500, 300));
            primaryStage.show();

            // Join the lobby chat
            client.joinChatRoom("lobby");

        }
        catch (Exception e) {
            e.printStackTrace();
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

    @FXML
    public void startGame(String[] command) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //TODO create a new game tab. add game to hashmap set controller see createNewChatroom func
            }
        });
    }

    /**
     * This method receives a message, and displays it in the lobby chat.
     * @param text
     */
    @FXML
    public void displayLobbyMessage(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run()
            {
                chatListView.getItems().add(text);
            }
        });
    }

    public void displayMessage(String[] text) {
        tabMap.get(text[1]).displayMessage(text[2] + ": " + text[3]);
    }

    public void chooseChatRoomName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New room");
        dialog.setHeaderText("Create your room");
        dialog.setContentText("Enter a unique room name:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(roomName -> client.joinChatRoom(roomName));
    }

    public void chooseGameRoomName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New room");
        dialog.setHeaderText("Create your room");
        dialog.setContentText("Enter a unique room name:");

        Optional<String> result = dialog.showAndWait();
        //newGame("PER");
        result.ifPresent(roomName -> client.joinGameRoom(roomName));
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
                    e.printStackTrace();
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

        System.exit(0);
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
                    gameController.setGameName(response[2]);
                    gameController.ludoBoard();
                   /* if (users != null && users.length > 0) {
                        tabMap.get(users[1]).updateUsers(users);
                    }*/
                    tab.setOnClosed(new EventHandler<Event>() {
                        @Override
                        public void handle(Event event) {
                            //TODO leave game
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void playerRoll(String command[]) {
        //gameMap.get(command[1]). //TODO player has rolled
    }

    public void playerTurn(String command[]) {
        //gameMap.get(command[1]). //TODO player turn
    }

    public void playerMove(String command[]) {
        //gameMap.get(command[1]). //TODO player has moved
    }


    public static void setClient(List<String> args) {
        client = new Client(args.get(0), args.get(1), args.get(2));
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void startScene(String[] args) {
        launch(args);
    }
}
