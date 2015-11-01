package no.hig.ezludo.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This class handles the GUI of the lobby, and whatever events may happen in the window.
 * Date: 30.10.2015
 * @author Per-Kristian Nilsen
 */
public class MainController extends Application {
    public ListView chatListView;
    public Scene lobbyScene;
    private Client client;

    public MainController(Client clientObject) {
        client = clientObject;
        launch(null);
    }


    /**
     * This method loads the lobby window.
     * @param primaryStage the JavaFX stage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
        primaryStage.setTitle("Ez-Ludo");
        primaryStage.setScene(lobbyScene = new Scene(root, 300, 275));
        primaryStage.show();
    }

    /**
     * This method is called when the user presses enter in the text field. It will send the message to the client
     * object, which further sends the message to the server.
     * @param event the triggered event of pressing enter in the text field
     */
    @FXML
    public void handleTextFieldEvent(ActionEvent event){
        TextField source = (TextField) event.getSource();
        client.sendChatMessage(source.getText());
        //TODO: The message sent will be returned to the client, so this method shouldn't display the message sent.
        displayLobbyMessage(source.getText());
    }

    /**
     * This method receives a message, and displays it in the lobby chat.
     * @param text
     */
    public void displayLobbyMessage(String text) {
        chatListView.getItems().add(text);
    }

    /*
    public static void main(String[] args) {
        launch(args);
    }
    */

}
