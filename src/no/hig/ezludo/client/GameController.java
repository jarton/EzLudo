package no.hig.ezludo.client;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
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

    //Players
    HashMap<String, String> players = new HashMap<>();

    // Dices
    @FXML private ImageView diceImage;
    private Image dice;
    private int diceNr;
    private final int diceMin = 1;
    private final int diceMax = 6;
    private final int rounds = 8;
    private int diceNrFromServer;

    // Ludo Pieces imageViews array
    @FXML private ImageView redPieces[] = new ImageView[4];
    @FXML private ImageView bluePieces[] = new ImageView[4];
    @FXML private ImageView yellowPieces[] = new ImageView[4];
    @FXML private ImageView greenPieces[] = new ImageView[4];

    // ludo pieces images
    private Image redImage[] = new Image[4];
    private Image yellowImage[] = new Image[4];
    private Image blueImage[] = new Image[4];
    private Image greenImage[] =  new Image[4];

    // single ludo pieces imageviews
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

    // Controller mechanism
    private boolean redInStart;
    private boolean blueInStart;
    private boolean yellowInStart;
    private boolean greenInStart;
    private boolean redInFinish;
    private boolean blueInFinish;
    private boolean yellowInFinish;
    private boolean greenInFinish;
    private boolean redInGoal;
    private boolean blueInGoal;
    private boolean yellowInGoal;
    private boolean greenInGoal;

    // Used if user have to move back
    private int moveBackNr;
    private boolean moveBack;


    //make array for each color with one int for each peice
    // Test
    String gameName;
    private int redCurrent;
    private int blueCurrent;
    private int greenCurrent;
    private int yellowCurrent;


    public GameController() { }

    // constructor: setter opp brett, trening og r�d brikke
    public void ludoBoard() {
        ludoBoardCoordinates = new LudoBoardCoordinates();
        board = new Image("/res/board.png");
        dice = new Image("/res/dices/dice1.png");
        this.ludoBoardImage.setImage(board);
        this.diceImage.setImage(dice);

        //set ImageViews in array
        redPieces[0] = red1View;
        redPieces[1] = red2View;
        redPieces[2] = red3View;
        redPieces[3] = red4View;
        bluePieces[0] = blue1View;
        bluePieces[1] = blue2View;
        bluePieces[2] = blue3View;
        bluePieces[3] = blue4View;
        greenPieces[0] = green1View;
        greenPieces[1] = green2View;
        greenPieces[2] = green3View;
        greenPieces[3] = green4View;
        yellowPieces[0] = yellow1View;
        yellowPieces[1] = yellow2View;
        yellowPieces[2] = yellow3View;
        yellowPieces[3] = yellow4View;

        // array of images instead of single vars
        for (int i=0;i<4;i++) {
            redImage[i] = new Image("/res/red2final.png");
            blueImage[i] = new Image("/res/blue2final.png");
            greenImage[i] = new Image("/res/green2final.png");
            yellowImage[i] = new Image("/res/yellow2final.png");
        }
        setupBoard();
    }

    // Set all chips in start pos
    public void setupBoard(){
        redInStart = true;
        blueInStart = true;
        yellowInStart = true;
        greenInStart = true;
        redInFinish = false;
        blueInFinish = false;
        greenInFinish = false;
        yellowInFinish = false;
        redInGoal = false;
        blueInGoal = false;
        greenInGoal = false;
        yellowInGoal = false;



        // array implemetation
        for (int i = 0; i < 4; i++) {

            this.redPieces[i].setX(ludoBoardCoordinates.redStart[i+1][1] * 600);
            this.redPieces[i].setY(ludoBoardCoordinates.redStart[i+1][2] * 600);
            this.redPieces[i].setImage(redImage[i]);

            this.bluePieces[i].setX(ludoBoardCoordinates.blueStart[i+1][1] * 600);
            this.bluePieces[i].setY(ludoBoardCoordinates.blueStart[i+1][2] * 600);
            this.bluePieces[i].setImage(blueImage[i]);

            this.greenPieces[i].setX(ludoBoardCoordinates.greenStart[i+1][1] * 600);
            this.greenPieces[i].setY(ludoBoardCoordinates.greenStart[i+1][2] * 600);
            this.greenPieces[i].setImage(greenImage[i]);

            this.yellowPieces[i].setX(ludoBoardCoordinates.yellowStart[i+1][1] * 600);
            this.yellowPieces[i].setY(ludoBoardCoordinates.yellowStart[i+1][2] * 600);
            this.yellowPieces[i].setImage(yellowImage[i]);
        }

        moveBackNr = 0;
        moveBack = false;
        redCurrent = 0;
        blueCurrent = 0;
        greenCurrent = 0;
        yellowCurrent = 0;
    }

    public void setupPlayers(String players[]) {
       this.players.put(players[0], "red");
        this.players.put(players[1], "green");
        this.players.put(players[2], "yellow");
        this.players.put(players[3], "blue");
    }

    public void setRedinMain () {
         redCurrent = 1;
         this.redPieces[0].setX(ludoBoardCoordinates.mainArea[redCurrent][1] * 600);
         this.redPieces[0].setY(ludoBoardCoordinates.mainArea[redCurrent][2] * 600);
         this.redPieces[0].setImage(redImage[0]);
     }

    public void setBlueinMain() {
        blueCurrent = 1;
        this.bluePieces[0].setX(ludoBoardCoordinates.mainArea[14][1] * 600);
        this.bluePieces[0].setY(ludoBoardCoordinates.mainArea[14][2] * 600);
        this.bluePieces[0].setImage(blueImage[0]);
    }

    public void playerMove(String command[]) {
        if (players.get(command[3]).equals("red")) {
           movePiece(Integer.parseInt(command[6]), redCurrent, redPieces[Integer.parseInt(command[5])],
                   redImage[Integer.parseInt(command[5])], ludoBoardCoordinates.redFinish, "red");
        }

        else if (players.get(command[3]).equals("blue")) {
            movePiece(Integer.parseInt(command[6]), blueCurrent, bluePieces[Integer.parseInt(command[5])],
                    blueImage[Integer.parseInt(command[5])], ludoBoardCoordinates.blueFinish, "blue");
        }

        else if (players.get(command[3]).equals("yellow")) {
            movePiece(Integer.parseInt(command[6]), yellowCurrent, yellowPieces[Integer.parseInt(command[5])],
                    yellowImage[Integer.parseInt(command[5])], ludoBoardCoordinates.yellowFinish, "yellow");
        }

        else if (players.get(command[3]).equals("green")) {
            movePiece(Integer.parseInt(command[6]), greenCurrent, greenPieces[Integer.parseInt(command[5])],
                    greenImage[Integer.parseInt(command[5])], ludoBoardCoordinates.greenFinish, "green");
        }


    }

    // Med animasjon, generell move for alle brikker og farger,
    public void movePiece(int steps, int colorCurrent, ImageView imageView, Image image,
                         double finishArray[][], String color) {
        int stop=colorCurrent+steps;
        Thread moveThread = new Thread(new Runnable() {
            public void run() {
                int colCurrent = colorCurrent;
                int squareInArray = 0;
                if (color.equals("red")) {
                    squareInArray = colCurrent;
                }
                else if (color.equals("blue")) {
                    squareInArray = colCurrent+14;
                }
                else if (color.equals("green")) {
                    squareInArray = colCurrent+39;
                }
                else if (color.equals("yellow")) {
                    squareInArray = colCurrent+ 27;
                }
                while (colCurrent+1 <= stop) {
                    colCurrent++;
                    squareInArray++;

                    // begynner p� 1 igjen etter main er ferdig
                    if(colCurrent== 52) {
                        if (color.equals("red")) {
                            imageView.setX(ludoBoardCoordinates.mainArea[1][1] * 600);
                            imageView.setY(ludoBoardCoordinates.mainArea[1][2] * 600);
                        }
                        else if (color.equals("blue")) {
                            imageView.setX(ludoBoardCoordinates.mainArea[14][1] * 600);
                            imageView.setY(ludoBoardCoordinates.mainArea[14][2] * 600);
                        }
                        else if (color.equals("green")) {
                            imageView.setX(ludoBoardCoordinates.mainArea[39][1] * 600);
                            imageView.setY(ludoBoardCoordinates.mainArea[39][2] * 600);
                        }
                        else if (color.equals("yellow")) {
                            imageView.setX(ludoBoardCoordinates.mainArea[27][1] * 600);
                            imageView.setY(ludoBoardCoordinates.mainArea[27][2] * 600);
                        }
                        imageView.setImage(image);
                        System.out.print(colCurrent);
                        System.out.print("\n");
                    }

                    //Flytter inn mot m�l BUG: flytter kun 1 og 1 rute
                    else if(colCurrent > 52 && colCurrent< 58) {
                        imageView.setX(finishArray[colCurrent - 52][1] * 600);
                        imageView.setY(finishArray[colCurrent - 52][2] * 600);
                        imageView.setImage(image);
                        System.out.print(colCurrent);
                        System.out.print("\n");
                    }

                    // Hvis spiller f�r terningkast som g�r utenfor brettet \ forbi m�l m� det flyttes tilbake.
                    else if (colCurrent > 58) {
                        // FUNKER IKKE
                        // g�r i loop
                        //    redCurrent--;
                        moveBack = true;
                        moveBackNr++;
                        System.out.print(stop + " stop");
                        System.out.print("\n");
                    }

                    // Spilleren er i m�l og skal ikke kunen flytte brukken noe mer.
                    else if (colCurrent == 58 && stop == 58) {
                        imageView.setX(finishArray[6][1] * 600);
                        imageView.setY(finishArray[6][2] * 600);
                        imageView.setImage(image);
                        redInGoal = true;
                        System.out.print(colCurrent);
                        System.out.print("\n");
                    }
                    else if(colCurrent < 52) {
                        // Flytter vanlig i main
                        if (color.equals("red")) {
                                imageView.setX(ludoBoardCoordinates.mainArea[squareInArray][1] * 600);
                                imageView.setY(ludoBoardCoordinates.mainArea[squareInArray][2] * 600);
                        }
                        else if (color.equals("blue")) {
                            if (squareInArray < 52) {
                                imageView.setX(ludoBoardCoordinates.mainArea[squareInArray][1] * 600);
                                imageView.setY(ludoBoardCoordinates.mainArea[squareInArray][2] * 600);
                            }
                            else {
                                imageView.setX(ludoBoardCoordinates.mainArea[squareInArray-51][1] * 600);
                                imageView.setY(ludoBoardCoordinates.mainArea[squareInArray-51][2] * 600);
                            }
                        }
                        else if (color.equals("green")) {
                            if (squareInArray < 52) {
                                imageView.setX(ludoBoardCoordinates.mainArea[squareInArray][1] * 600);
                                imageView.setY(ludoBoardCoordinates.mainArea[squareInArray][2] * 600);
                            }
                            else {
                                imageView.setX(ludoBoardCoordinates.mainArea[squareInArray-51][1] * 600);
                                imageView.setY(ludoBoardCoordinates.mainArea[squareInArray-51][2] * 600);
                            }
                        }
                        else if (color.equals("yellow")) {

                            if (squareInArray < 52) {
                                imageView.setX(ludoBoardCoordinates.mainArea[squareInArray][1] * 600);
                                imageView.setY(ludoBoardCoordinates.mainArea[squareInArray][2] * 600);
                            }
                            else {
                                imageView.setX(ludoBoardCoordinates.mainArea[squareInArray-51][1] * 600);
                                imageView.setY(ludoBoardCoordinates.mainArea[squareInArray-51][2] * 600);
                            }
                        }
                        imageView.setImage(image);
                        System.out.print(colCurrent);
                        System.out.print("\n");
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(moveBack == true) {
                    moveBack(moveBackNr, finishArray, color, imageView, image, colCurrent);
                    System.out.print("moveBack()");
                    System.out.print("\n");

                }
                else {
                    if (color.equals("red")) {
                        redCurrent = colCurrent;
                    }
                    else if (color.equals("blue")) {
                        blueCurrent = colCurrent;
                    }
                    else if (color.equals("yellow")) {
                        yellowCurrent = colCurrent;
                    }
                    else if (color.equals("green")) {
                        greenCurrent = colCurrent;
                    }
                }
            }
        });
        moveThread.start();
    }

    public void moveBack(int nr, double finish[][], String color, ImageView imageView, Image image, int colCurrent) {
        Thread movingThread = new Thread(new Runnable() {
            public void run() {
                int tempCurrent = colCurrent;
                tempCurrent -= nr -1;
                int i = 6;
                int j = i - nr;
                moveBackNr = 0;
                moveBack = false;
                while (i >= j) {
                    System.out.println(tempCurrent);
                    imageView.setX(finish[i][1] * 600);
                    imageView.setY(finish[i][2] * 600);
                    imageView.setImage(image);
                    i--;
                    tempCurrent--;
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                }
                if (color.equals("red")) {
                    redCurrent = tempCurrent;
                }
                else if (color.equals("blue")) {
                    blueCurrent = tempCurrent;
                }
                else if (color.equals("yellow")) {
                    yellowCurrent = tempCurrent;
                }
                else if (color.equals("green")) {
                    greenCurrent = tempCurrent;
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
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // TODO Connect to server and get random int as int diceNrFromServ
                diceNrFromServer = diceNr;
                dice = new Image("/res/dices/dice" + diceNrFromServer+".png");
                diceImage.setImage(dice);

                // hvis red ikke st�r i start omr�de
                if (blueInStart == false) {
                    //redMove(diceNrFromServer);
                    movePiece(diceNrFromServer, blueCurrent, bluePieces[0], blueImage[0], ludoBoardCoordinates.blueFinish, "blue");
                }

                // hvis red st�r i start omr og tering viser 6 = flytt ut til main array
                if (diceNr == 6 && blueInStart == true && blueInGoal == false) {
                    blueInStart = false;
                    setBlueinMain();
                }

                if (diceNr == 6 && blueInStart== true && blueInGoal == true) {
                    blueInStart = false;
                    setBlueinMain();
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
