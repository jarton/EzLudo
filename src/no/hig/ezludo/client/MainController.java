package no.hig.ezludo.client;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
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
    public ListView chatListView;
    public Scene lobbyScene;
//    public Pane ludoBoard;


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
            client.setMainController(this);

            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Lobby.fxml"));
            Parent root = (Parent) loader.load();

            client.setMainController(loader.getController());

            // Ludo board
           // final SwingNode swingNode = new SwingNode();
          //  createAndSetSwingContent(swingNode);
          //  ludoBoard.getChildren().add(swingNode); // Adding swing node
            ///////////////

            primaryStage.setTitle("Ez-Ludo");
            primaryStage.setScene(lobbyScene = new Scene(root, 500, 300));
            primaryStage.show();
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
        //TODO: why is the client object null???!
        if (client != null) {
            //TODO this function is bs
            client.sendChatMessage(source.getText(), "0");
        }
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
        dialog.setTitle("New chat room");
        dialog.setHeaderText("Create your chat room");
        dialog.setContentText("Enter a unique room name:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(roomName -> client.joinChatRoom(roomName));
    }

    public void newChatRoom(String[] response) {
        //TODO Let user decide chat room name
        //TODO Send to server and get response
        Platform.runLater(new Runnable() {
            @Override
            public void run()
            {
                Tab tab = new Tab(response[1]);
                tabPane.getTabs().add(tab);
                try {
                    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Chatroom.fxml"));
                    tab.setContent((Node) loader.load());
                    ChatController chatController = loader.getController();
                    tabMap.put(response[1], chatController);
                    chatController.setRoomName(response[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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

/*
    // Get swing. add to javafx pane
    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel panel = new LudoBoard();
                swingNode.setContent(panel);
            }
        });
    } */
}
