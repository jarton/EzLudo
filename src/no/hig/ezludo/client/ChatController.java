package no.hig.ezludo.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * @author piddy
 * date 11/11/15.
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
        MainController.getClient().sendChatMessage(source.getText(), roomName);
        source.clear();
    }

    /**
     * This method receives a message, and displays it in the lobby chat.
     * @param text The message
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

    /**
     * This method updates the list of connected users in a chat room. This is done in a separate thread.
     * @param command The command
     */
    public void updateUsers(String[] command) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Clear the user list
                userListView.getItems().clear();
                // Add every user in the command array into the user list
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
