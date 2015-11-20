package no.hig.ezludo.client;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.util.Random;

/**
 * Created by Kristian on 12.11.2015.
 */
public class GameController {

    //Ludo board
    LudoBoardCoordinates ludoBoardCoordinates;
    @FXML private ImageView ludoBoardImage;
    private Image board;

    // Dices
    @FXML private ImageView diceImage;
    private Image dice;
    private int diceNr;
    private final int diceMin = 1;
    private final int diceMax = 6;
    private final int rounds = 8;
    private int diceNrFromServ;



    // Ludo Pieces
    @FXML private ImageView red1View;
    @FXML private ImageView red2View;
    @FXML private ImageView red3View;
    @FXML private ImageView red4View;
    @FXML private ImageView blue1View;
    @FXML private ImageView blue2View;
    @FXML private ImageView blue3View;
    @FXML private ImageView blue4View;
    @FXML private ImageView yellow1View;
    @FXML private ImageView yellow2View;
    @FXML private ImageView yellow3View;
    @FXML private ImageView yellow4View;
    @FXML private ImageView green1View;
    @FXML private ImageView green2View;
    @FXML private ImageView green3View;
    @FXML private ImageView greeb4View;
    private Image red1Image;
    private Image red2Image;
    private Image red3Image;
    private Image red4Image;
    private Image blue1Image;
    private Image blue2Image;
    private Image blue3Image;
    private Image blue4Image;
    private Image yellow1Image;
    private Image yellow2Image;
    private Image yellow3Image;
    private Image yellow4Image;
    private Image green1Image;
    private Image green2Image;
    private Image green3Image;
    private Image greeb4Image;

    // Controller mechanism
    private boolean redInStart;
    private boolean blueInStart;
    private boolean yellowInStart;
    private boolean greenInStart;
    private boolean redInFinish;
    private boolean blueInFinish;
    private boolean yellowInFinish;
    private boolean greenInFinish;


    // Test
    @FXML private ImageView redImage;
    private Image red;
    String gameName;
    private int redCurrent = 0;


    public GameController() { }

    // constructor: setter opp brett, trening og rød brikke
    public void ludoBoard() {
        ludoBoardCoordinates = new LudoBoardCoordinates();
        board = new Image("/res/board.png");
        dice = new Image("/res/dices/dice1.png");
        red = new Image("/res/red2final.png");
        this.ludoBoardImage.setImage(board);
        this.diceImage.setImage(dice);
        setupBoard();
    }

    // Set all chips in start pos
    public void setupBoard() {
        redInStart = true;
        blueInStart = true;
        yellowInStart = true;
        greenInStart = true;
        redInFinish = false;

        // Set red start pos
        this.redImage.setX(ludoBoardCoordinates.redStart[1][1] * 600);
        this.redImage.setY(ludoBoardCoordinates.redStart[1][2] * 600);
        this.redImage.setImage(red);
    }

    // Setter red over til main array etter red får 6 på terning
    public void setRedinMain () {
        redCurrent = 1;
        this.redImage.setX(ludoBoardCoordinates.mainArea[1][1] * 600);
        this.redImage.setY(ludoBoardCoordinates.mainArea[1][2] * 600);
        this.redImage.setImage(red);
    }

    // flytter red ihht nr på tering
    public void redMove(int nr) {
        if (redCurrent+nr > 51) {
            // Funker ikke må innom main 1
            int finalStep = 51 - (redCurrent+nr);
            this.redImage.setX(ludoBoardCoordinates.redFinish[finalStep][1] * 600);
            this.redImage.setY(ludoBoardCoordinates.redFinish[finalStep][2] * 600);
            this.redImage.setImage(red);
            // ny array = redGoal
        }
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

                // hvis red ikke står i start område
                if (redInStart == false) {
                    redMove(diceNrFromServ);
                }

                // hvis red står i start omr og tering viser 6 = flytt ut til main array
                if (diceNr == 6 && redInStart == true) {
                    redInStart = false;
                    setRedinMain();
                }

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
