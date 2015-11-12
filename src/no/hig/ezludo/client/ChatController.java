package no.hig.ezludo.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Created by piddy on 11/11/15.
 */
public class ChatController {

    private String id;
    @FXML private ListView chatListView;

    /**
     * This method is called when the user presses enter in the text field. It will send the message to the client
     * object, which further sends the message to the server.
     * @param event the triggered event of pressing enter in the text field
     */
    @FXML
    public void handleTextFieldEvent(ActionEvent event){
        TextField source = (TextField) event.getSource();
        //TODO: why is the client object null???!
        MainController.client.sendChatMessage(source.getText(), id);
    }

    /**
     * This method receives a message, and displays it in the lobby chat.
     * @param text
     */
    @FXML
    public void displayMessage(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatListView.getItems().add(text);
            }
        });
    }

    public void setId(String id) {
        this.id = id;
    }
}
