package no.hig.ezludo.client;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Created by piddy on 11/11/15.
 */
public class ChatController {

    private String roomName;
    @FXML private ListView chatListView;
    @FXML private ListView userListView;

    /**
     * This method is called when the user presses enter in the text field. It will send the message to the client
     * object, which further sends the message to the server.
     * @param event the triggered event of pressing enter in the text field
     */
    @FXML
    public void handleTextFieldEvent(ActionEvent event){
        TextField source = (TextField) event.getSource();
        MainController.client.sendChatMessage(source.getText(), roomName);
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

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void updateUsers(String[] command) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                userListView.getItems().clear();
                for (int i = 2; i < command.length; i++) {
                    userListView.getItems().add(command[i]);
                }
            }
        });
    }

    public String getRoomName() {
        return roomName;
    }
}
