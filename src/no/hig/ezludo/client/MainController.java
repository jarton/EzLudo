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


public class MainController extends Application {
    public ListView chatListView;

    public Scene lobbyScene;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
        primaryStage.setTitle("Ez-Ludo");
        primaryStage.setScene(lobbyScene = new Scene(root, 300, 275));
        primaryStage.show();
    }

    @FXML
    public void handleTextFieldEvent(ActionEvent event){
        TextField source = (TextField) event.getSource();
        System.out.println(source.getText());
        inputTextToChat(source.getText());
        // TODO: Pass chat text to Client.
    }

    public void inputTextToChat(String text) {
        chatListView.getItems().add(text);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
