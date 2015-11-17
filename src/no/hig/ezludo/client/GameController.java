package no.hig.ezludo.client;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;

/**
 * Created by Kristian on 12.11.2015.
 */
public class GameController {
    @FXML private ListView chatListView;
    @FXML private ListView userListView;
    @FXML private ImageView ludoBoardImage;
    private Dimension size;
    private java.awt.Image board;
    String gameName;


    public GameController() { }

    public void ludoBoard() {
        // get Ludo board Image
       /* ImageIcon tmpBoard = new ImageIcon (getClass().getResource("/res/board.png"));
        int height = tmpBoard.getIconHeight();
        int width = tmpBoard.getIconWidth();
        size = new Dimension(width, height);
        board = tmpBoard.getImage();*/

        Image img = new Image("/res/board.png");
        this.ludoBoardImage = new ImageView();
        this.ludoBoardImage.setImage(img);

    }

    public String getGameName() { return gameName; }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }




}
