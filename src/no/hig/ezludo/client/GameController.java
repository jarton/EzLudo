package no.hig.ezludo.client;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;

import javax.swing.text.html.ImageView;
import java.io.File;
import java.util.Random;

/**
 * Created by Kristian on 12.11.2015.
 */
public class GameController {
    @FXML private ListView chatListView;
    @FXML private ListView userListView;
    String gameName;


    public GameController() { }

    public String getGameName() { return gameName; }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }




}
