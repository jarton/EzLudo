package no.hig.ezludo.client;

import com.sun.xml.internal.rngom.digested.DDataPattern;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sun.applet.Main;

import java.util.List;
import java.util.Map;

/**
 * This class handles the GUI of the lobby, and whatever events may happen in the window.
 * Date: 30.10.2015
 * @author Per-Kristian Nilsen
 */
public class MainController extends Application {
    public ListView chatListView;
    public Scene lobbyScene;

    @FXML public Client client;

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
            client = new Client(rawArguments.get(0), rawArguments.get(1), rawArguments.get(2), this);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Lobby.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
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
            client.sendChatMessage(source.getText());
        }
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

    public static void main(String[] args) {
        launch(args);
    }

    public static void startScene(String[] args) {
        launch(args);
    }
}
