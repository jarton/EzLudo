package no.hig.ezludo.client;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by Kristian on 12.11.2015.
 */
public class GameController {
    @FXML private ListView chatListView;
    @FXML private ListView userListView;
    @FXML private ImageView ludoBoardImage;
    @FXML private ImageView diceImage;
    @FXML private ImageView redImage;
    private Dimension size;
    private Image board;
    private Image dice;
    private Image red;
    String gameName;
    private int diceNr;
    private final int diceMin = 1;
    private final int diceMax = 6;
    private final int rounds = 8;
    private int diceNrFromServ;
    LudoBoardCoordinates ludoBoardCoordinates;
    private int redCurrent = 0;


    public GameController() { }

    public void ludoBoard() {
        ludoBoardCoordinates = new LudoBoardCoordinates();
        board = new Image("/res/board.png");
        dice = new Image("/res/dices/dice1.png");
        red = new Image("/res/red2final.png");
        this.ludoBoardImage.setImage(board);
        this.diceImage.setImage(dice);
        redMove(1);
    }

    public void redMove(int nr) {
        redCurrent += nr;
        this.redImage.setX(ludoBoardCoordinates.mainArea[redCurrent][1] * 600);
        this.redImage.setY(ludoBoardCoordinates.mainArea[redCurrent][2] * 600);
        this.redImage.setImage(red);
    }

    public void rollDices()  {
        Thread diceLoadingThread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i<=rounds; i++) {
                    diceNr = randomInt(diceMin, diceMax);
                    showImage(diceNr);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // TODO Connect to server and get random int as int diceNrFromServ
                diceNrFromServ = diceNr;
                dice = new Image("/res/dices/dice"+diceNrFromServ+".png");
                diceImage.setImage(dice);
                redMove(diceNrFromServ);
            }
        });
        diceLoadingThread.start();

    }

    public static int randomInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public void showImage(int nr) {
        dice = new Image("/res/dices/dice"+nr+".png");
        this.diceImage.setImage(dice);
    }

    public String getGameName() { return gameName; }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }




}
