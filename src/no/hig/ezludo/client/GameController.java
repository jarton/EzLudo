package no.hig.ezludo.client;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Random;

/**
 * @author Kristian
 * date 12.11.2015.
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
    @FXML private ImageView green4View;
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
    private Image green4Image;

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
    private int redCurrent;


    public GameController() { }

    // constructor: setter opp brett, trening og rød brikke
    public void ludoBoard() {
        ludoBoardCoordinates = new LudoBoardCoordinates();
        board = new Image("/res/board.png");
        dice = new Image("/res/dices/dice1.png");
        this.ludoBoardImage.setImage(board);
        this.diceImage.setImage(dice);

        red1Image = new Image("/res/red2final.png");
        red2Image = new Image("/res/red2final.png");
        red3Image = new Image("/res/red2final.png");
        red4Image = new Image("/res/red2final.png");
        blue1Image = new Image("/res/blue2final.png");
        blue2Image = new Image("/res/blue2final.png");
        blue3Image = new Image("/res/blue2final.png");
        blue4Image = new Image("/res/blue2final.png");
        green1Image = new Image("/res/green2final.png");
        green2Image = new Image("/res/green2final.png");
        green3Image = new Image("/res/green2final.png");
        green4Image = new Image("/res/green2final.png");
        yellow1Image = new Image("/res/yellow2final.png");
        yellow2Image = new Image("/res/yellow2final.png");
        yellow3Image = new Image("/res/yellow2final.png");
        yellow4Image = new Image("/res/yellow2final.png");

        setupBoard();
    }

    // Set all chips in start pos
    public void setupBoard() {
        redInStart = true;
        blueInStart = true;
        yellowInStart = true;
        greenInStart = true;
        redInFinish = false;
        blueInFinish = false;
        greenInFinish = false;
        yellowInFinish = false;

        // Set red start pos
        this.red1View.setX(ludoBoardCoordinates.redStart[1][1] * 600);
        this.red1View.setY(ludoBoardCoordinates.redStart[1][2] * 600);
        this.red1View.setImage(red1Image);
        this.red2View.setX(ludoBoardCoordinates.redStart[2][1] * 600);
        this.red2View.setY(ludoBoardCoordinates.redStart[2][2] * 600);
        this.red2View.setImage(red2Image);
        this.red3View.setX(ludoBoardCoordinates.redStart[3][1] * 600);
        this.red3View.setY(ludoBoardCoordinates.redStart[3][2] * 600);
        this.red3View.setImage(red3Image);
        this.red4View.setX(ludoBoardCoordinates.redStart[4][1] * 600);
        this.red4View.setY(ludoBoardCoordinates.redStart[4][2] * 600);
        this.red4View.setImage(red4Image);

        this.blue1View.setX(ludoBoardCoordinates.blueStart[1][1] * 600);
        this.blue1View.setY(ludoBoardCoordinates.blueStart[1][2] * 600);
        this.blue1View.setImage(blue1Image);
        this.blue2View.setX(ludoBoardCoordinates.blueStart[2][1] * 600);
        this.blue2View.setY(ludoBoardCoordinates.blueStart[2][2] * 600);
        this.blue2View.setImage(blue2Image);
        this.blue3View.setX(ludoBoardCoordinates.blueStart[3][1] * 600);
        this.blue3View.setY(ludoBoardCoordinates.blueStart[3][2] * 600);
        this.blue3View.setImage(blue3Image);
        this.blue4View.setX(ludoBoardCoordinates.blueStart[4][1] * 600);
        this.blue4View.setY(ludoBoardCoordinates.blueStart[4][2] * 600);
        this.blue4View.setImage(blue4Image);

        this.green1View.setX(ludoBoardCoordinates.greenStart[1][1] * 600);
        this.green1View.setY(ludoBoardCoordinates.greenStart[1][2] * 600);
        this.green1View.setImage(green1Image);
        this.green2View.setX(ludoBoardCoordinates.greenStart[2][1] * 600);
        this.green2View.setY(ludoBoardCoordinates.greenStart[2][2] * 600);
        this.green2View.setImage(green2Image);
        this.green3View.setX(ludoBoardCoordinates.greenStart[3][1] * 600);
        this.green3View.setY(ludoBoardCoordinates.greenStart[3][2] * 600);
        this.green3View.setImage(green3Image);
        this.green4View.setX(ludoBoardCoordinates.greenStart[4][1] * 600);
        this.green4View.setY(ludoBoardCoordinates.greenStart[4][2] * 600);
        this.green4View.setImage(green4Image);

        this.yellow1View.setX(ludoBoardCoordinates.yellowStart[1][1] * 600);
        this.yellow1View.setY(ludoBoardCoordinates.yellowStart[1][2] * 600);
        this.yellow1View.setImage(yellow1Image);
        this.yellow2View.setX(ludoBoardCoordinates.yellowStart[2][1] * 600);
        this.yellow2View.setY(ludoBoardCoordinates.yellowStart[2][2] * 600);
        this.yellow2View.setImage(yellow2Image);
        this.yellow3View.setX(ludoBoardCoordinates.yellowStart[3][1] * 600);
        this.yellow3View.setY(ludoBoardCoordinates.yellowStart[3][2] * 600);
        this.yellow3View.setImage(yellow3Image);
        this.yellow4View.setX(ludoBoardCoordinates.yellowStart[4][1] * 600);
        this.yellow4View.setY(ludoBoardCoordinates.yellowStart[4][2] * 600);
        this.yellow4View.setImage(yellow4Image);

        redCurrent = 0;
    }

    // Setter red over til main array etter red får 6 på terning
    public void setRedinMain () {
        redCurrent = 1;
        this.red1View.setX(ludoBoardCoordinates.mainArea[redCurrent][1] * 600);
        this.red1View.setY(ludoBoardCoordinates.mainArea[redCurrent][2] * 600);
        this.red1View.setImage(red1Image);
    }

    // Med animasjon
    public void redMove(int nr) {
        int stop=redCurrent+nr;
        Thread movingThread = new Thread(new Runnable() {
            public void run() {
                while (redCurrent+1 <= stop) {
                //for (int i = redCurrent; i<=(redCurrent+nr+1); i++) {
                    redCurrent++;
                    red1View.setX(ludoBoardCoordinates.mainArea[redCurrent][1] * 600);
                    red1View.setY(ludoBoardCoordinates.mainArea[redCurrent][2] * 600);
                    red1View.setImage(red1Image);
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        movingThread.start();
    }

    public void rollDices()  {
        Thread diceLoadingThread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i<=rounds; i++) {
                    diceNr = randomInt(diceMin, diceMax);
                    showImage(diceNr);
                    try {
                        Thread.sleep(150);
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
                    //redMove(diceNrFromServ);
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
