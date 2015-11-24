package no.hig.ezludo.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author Kristian
 * date 12.11.2015.
 */
public class GameController {

    // CONSTANTS
    final private int MOVEPIECELAG = 500;
    final private int DICELAG = 100;

    //Game info
    String gameName;
    String gameId;

    //Ludo board
    LudoBoardCoordinates ludoBoardCoordinates;
    @FXML private ImageView ludoBoardImage;
    private Image board;
    private ArrayList<EventHandler<MouseEvent>> events = new ArrayList<>();

    //Players
    @FXML TextField label;
    HashMap<String, String> players = new HashMap<>();
    String myNickname;
    boolean yourTurn = false;

    //chat/Info view
    @FXML ListView chatView;

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

    private boolean redInFinish;
    private boolean blueInFinish;
    private boolean yellowInFinish;
    private boolean greenInFinish;

    private boolean[] redInStart = new boolean[4];
    private boolean[] blueInStart = new boolean[4];
    private boolean[] yellowInStart = new boolean[4];
    private boolean[] greenInStart = new boolean[4];

    private boolean[] redInGoal = new boolean[4];
    private boolean[] blueInGoal = new boolean[4];
    private boolean[] yellowInGoal = new boolean[4];
    private boolean[] greenInGoal = new boolean[4];

    // Used if user have to move back
    private int moveBackNr;
    private boolean moveBack;


    //make array for each color with one int for each peice
    // Test
    private int[] redCurrent = new int[4];
    private int[] blueCurrent = new int[4];
    private int[] greenCurrent = new int[4];
    private int[] yellowCurrent = new int[4];


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

        redInFinish = false;
        blueInFinish = false;
        greenInFinish = false;
        yellowInFinish = false;

        // array implemetation
        for (int i = 0; i < 4; i++) {

            this.redPieces[i].setX(ludoBoardCoordinates.redStart[i + 1][1] * 600);
            this.redPieces[i].setY(ludoBoardCoordinates.redStart[i + 1][2] * 600);
            this.redPieces[i].setImage(redImage[i]);

            this.bluePieces[i].setX(ludoBoardCoordinates.blueStart[i + 1][1] * 600);
            this.bluePieces[i].setY(ludoBoardCoordinates.blueStart[i + 1][2] * 600);
            this.bluePieces[i].setImage(blueImage[i]);

            this.greenPieces[i].setX(ludoBoardCoordinates.greenStart[i + 1][1] * 600);
            this.greenPieces[i].setY(ludoBoardCoordinates.greenStart[i + 1][2] * 600);
            this.greenPieces[i].setImage(greenImage[i]);

            this.yellowPieces[i].setX(ludoBoardCoordinates.yellowStart[i + 1][1] * 600);
            this.yellowPieces[i].setY(ludoBoardCoordinates.yellowStart[i + 1][2] * 600);
            this.yellowPieces[i].setImage(yellowImage[i]);

            redInStart[i] = true;
            blueInStart[i] = true;
            yellowInStart[i] = true;
            greenInStart[i] = true;

            redCurrent[i] = 0;
            blueCurrent[i] = 0;
            greenCurrent[i] = 0;
            yellowCurrent[i] = 0;

            redInGoal[i] = false;
            blueInGoal[i] = false;
            greenInGoal[i] = false;
            yellowInGoal[i] = false;


        }

        moveBackNr = 0;
        moveBack = false;
    }

    public void setupPlayers(String players[], String nickname) {
       this.players.put(players[0], "red");
        this.players.put(players[1], "green");
        this.players.put(players[2], "yellow");
        this.players.put(players[3], "blue");
        myNickname = nickname;
        label.setText("\t\t red: " + players[0] + "\t\t green: " + players[1] +
                "\t\t yellow: " + players[2] + "\t\t blue: " + players[3]);
    }

    public void setRedinMain () {
         redCurrent[0] = 1;
         this.redPieces[0].setX(ludoBoardCoordinates.mainArea[redCurrent[0]][1] * 600);
         this.redPieces[0].setY(ludoBoardCoordinates.mainArea[redCurrent[0]][2] * 600);
         this.redPieces[0].setImage(redImage[0]);
     }

    public void setBlueinMain() {
        blueCurrent[0] = 1;
        this.bluePieces[0].setX(ludoBoardCoordinates.mainArea[14][1] * 600);
        this.bluePieces[0].setY(ludoBoardCoordinates.mainArea[14][2] * 600);
        this.bluePieces[0].setImage(blueImage[0]);
    }

    @FXML
    public void playerRoll(String command[]) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatView.getItems().add(command[4] + "\trolled\t" + command[5]);
            }
        });
        if (command[4].equals(myNickname)) {
            showDices(command[5]);
            ImageView pieceArray[];
            int colorCurrent[];
            if (players.get(myNickname).equals("red")) {
                pieceArray = redPieces;
                colorCurrent = redCurrent;
            }
            else if (players.get(myNickname).equals("blue")) {
                pieceArray = bluePieces;
                colorCurrent = blueCurrent;
            }
            else if (players.get(myNickname).equals("yellow")) {
                pieceArray = yellowPieces;
                colorCurrent = yellowCurrent;
            }
            else {
                pieceArray = greenPieces;
                colorCurrent = greenCurrent;
            }
            boolean allInStart = true;
            for (int i=0;i<4;i++) {
                int pieceToMove = i;
                if (colorCurrent[i] != 0 || command[5].equals("6")) {
                    allInStart = false;
                    EventHandler<MouseEvent> event;
                    pieceArray[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event = new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println("cliecked");
                            MainController.client.movePiece(gameId, gameName, String.valueOf(pieceToMove));
                            movedPiece(pieceArray);
                        }
                    });
                    events.add(i, event);
                }
                if (allInStart)
                    MainController.client.movePiece(gameId, gameName, "0");
            }
        }
        else {
            showDices(command[5]);
        }
    }

    public void movedPiece(ImageView array[]) {
        for (int i=0;i<4;i++) {
            if (events.size() >= i)
                array[i].removeEventHandler(MouseEvent.MOUSE_CLICKED, events.get(i));
        }
        events.clear();
    }

    @FXML
    public void playerTurn(String command[]) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                displayMessage(command[4] + "'s turn");
            }
        });
        if (command[4].equals(myNickname)) {
            yourTurn = true;
        }
    }
    // ADDED 23.11: && <color>InGoal[Integer.parseInt(command[5])]  == false
       // If piece is in goal (colCurrent == 58)
        // Kristian

    public void playerMove(String command[]) {
        if (players.get(command[4]).equals("red") && redInGoal[Integer.parseInt(command[5])] == false) {
           movePiece(Integer.parseInt(command[6]), redCurrent[Integer.parseInt(command[5])],
                   redPieces[Integer.parseInt(command[5])], redImage[Integer.parseInt(command[5])],
                   Integer.parseInt(command[5]), ludoBoardCoordinates.redFinish, "red");
        }

        else if (players.get(command[4]).equals("blue") && blueInGoal[Integer.parseInt(command[5])] == false) {
            movePiece(Integer.parseInt(command[6]), blueCurrent[Integer.parseInt(command[5])],
                    bluePieces[Integer.parseInt(command[5])], blueImage[Integer.parseInt(command[5])],
                    Integer.parseInt(command[5]), ludoBoardCoordinates.blueFinish, "blue");
        }

        else if (players.get(command[4]).equals("yellow") && yellowInGoal[Integer.parseInt(command[5])] == false) {
            movePiece(Integer.parseInt(command[6]), yellowCurrent[Integer.parseInt(command[5])],
                    yellowPieces[Integer.parseInt(command[5])], yellowImage[Integer.parseInt(command[5])],
                    Integer.parseInt(command[5]), ludoBoardCoordinates.yellowFinish, "yellow");
        }

        else if (players.get(command[4]).equals("green") && greenInGoal[Integer.parseInt(command[5])] == false) {
            movePiece(Integer.parseInt(command[6]), greenCurrent[Integer.parseInt(command[5])],
                    greenPieces[Integer.parseInt(command[5])], greenImage[Integer.parseInt(command[5])],
                    Integer.parseInt(command[5]), ludoBoardCoordinates.greenFinish, "green");
        }
    }

    // Med animasjon, generell move for alle brikker og farger,
    public void movePiece(int steps, int colorCurrent, ImageView imageView, Image image,
                         int piece, double finishArray[][], String color) {
        int stop=steps;
        Thread moveThread = new Thread(new Runnable() {
            public void run() {
                int colCurrent = colorCurrent;
                int squareInArray = 0;
                if (color.equals("red")) {
                    squareInArray = colCurrent;
                }
                else if (color.equals("blue")) {
                    squareInArray = colCurrent+13;
                }
                else if (color.equals("green")) {
                    squareInArray = colCurrent+39;
                }
                else if (color.equals("yellow")) {
                    squareInArray = colCurrent+ 26;
                }
                while (colCurrent+1 <= stop) {
                    colCurrent++;
                    squareInArray++;

                    // begynner p� 1 igjen etter main er ferdig
                    if(colCurrent == 53) {
                        if (color.equals("red")) {
                            imageView.setX(ludoBoardCoordinates.mainArea[1][1] * 600);
                            imageView.setY(ludoBoardCoordinates.mainArea[1][2] * 600);
                        }
                        else if (color.equals("blue")) {
                            imageView.setX(ludoBoardCoordinates.mainArea[14][1] * 600);
                            imageView.setY(ludoBoardCoordinates.mainArea[14][2] * 600);
                        }
                        else if (color.equals("green")) {
                            imageView.setX(ludoBoardCoordinates.mainArea[40][1] * 600);
                            imageView.setY(ludoBoardCoordinates.mainArea[40][2] * 600);
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
                    else if(colCurrent > 53 && colCurrent < 59) {
                        imageView.setX(finishArray[colCurrent - 52][1] * 600);
                        imageView.setY(finishArray[colCurrent - 52][2] * 600);
                        imageView.setImage(image);
                        System.out.print(colCurrent);
                        System.out.print("\n");
                    }

                    // Hvis spiller f�r terningkast som g�r utenfor brettet \ forbi m�l m� det flyttes tilbake.
                    else if (colCurrent > 59) {
                        // FUNKER IKKE
                        // g�r i loop
                        //    redCurrent--;
                        moveBack = true;
                        moveBackNr++;
                        System.out.print(stop + " stop");
                        System.out.print("\n");
                    }

                    // Spilleren er i m�l og skal ikke kunen flytte brukken noe mer.
                    else if (colCurrent == 59 && stop == 59) {
                        imageView.setX(finishArray[6][1] * 600);
                        imageView.setY(finishArray[6][2] * 600);
                        imageView.setImage(image);

                       ////////// ADDED 23.11 Kristian
                        // if colCurrent is 58.. goal array == true.
                        // not possible to move piece anymore

                        if (color.equals("red")) {
                            redInGoal[piece] = true;
                        }
                        else if (color.equals("blue")) {
                            blueInGoal[piece] = true;
                        }
                        else if (color.equals("green")) {
                            greenInGoal[piece] = true;
                        }
                        else if (color.equals("yellow")) {
                            yellowInGoal[piece] = true;
                        }

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
                        Thread.sleep(MOVEPIECELAG);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(moveBack == true) {
                    moveBack(moveBackNr, finishArray, color, imageView, image, piece, colCurrent);
                    System.out.print("moveBack()");
                    System.out.print("\n");

                }
                else {
                    if (color.equals("red")) {
                        redCurrent[piece] = colCurrent;
                    }
                    else if (color.equals("blue")) {
                        blueCurrent[piece] = colCurrent;
                    }
                    else if (color.equals("yellow")) {
                        yellowCurrent[piece] = colCurrent;
                    }
                    else if (color.equals("green")) {
                        greenCurrent[piece] = colCurrent;
                    }
                }
            }
        });
        moveThread.start();
    }

    public void moveBack(int nr, double finish[][], String color, ImageView imageView, Image image,
                         int piece, int colCurrent) {
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
                            Thread.sleep(MOVEPIECELAG);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                }
                if (color.equals("red")) {
                    redCurrent[piece] = tempCurrent;
                }
                else if (color.equals("blue")) {
                    blueCurrent[piece] = tempCurrent;
                }
                else if (color.equals("yellow")) {
                    yellowCurrent[piece] = tempCurrent;
                }
                else if (color.equals("green")) {
                    greenCurrent[piece] = tempCurrent;
                }
            }
        });
        movingThread.start();
    }

    public void rollDices() {
        if (yourTurn) {
            MainController.client.rollDice(gameId, gameName);
            yourTurn = false;
        }
    }

    public void showDices(String nrToShow)  {
        Thread diceLoadingThread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i<=rounds; i++) {
                    diceNr = randomInt(diceMin, diceMax);
                    showImage(diceNr);
                    try {
                        Thread.sleep(DICELAG);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                dice = new Image("/res/dices/dice" + Integer.parseInt(nrToShow) +".png");
                diceImage.setImage(dice);

               // // hvis red ikke st�r i start omr�de
               // if (blueInStart == false) {
               //     //redMove(diceNrFromServer);
               //     movePiece(diceNrFromServer+blueCurrent[0], blueCurrent[0], bluePieces[0], blueImage[0],
               //             0, ludoBoardCoordinates.blueFinish, "blue");
               // }

               // // hvis red st�r i start omr og tering viser 6 = flytt ut til main array
               // if (diceNr == 6 && blueInStart == true && blueInGoal == false) {
               //     blueInStart = false;
               //     setBlueinMain();
               // }

               // if (diceNr == 6 && blueInStart== true && blueInGoal == true) {
               //     blueInStart = false;
               //     setBlueinMain();
               // }
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

    /**
     * This method is called when the user presses enter in the text field. It will send the message to the client
     * object, which further sends the message to the server.
     * @param event the triggered event of pressing enter in the text field
     */
    @FXML
    public void handleTextFieldEvent(ActionEvent event){
        TextField source = (TextField) event.getSource();
        MainController.client.sendGameMessage(source.getText(), gameId, gameName);
        source.clear();
    }

    /**
     * This method receives a message, and displays it in the game chat.
     * @param text
     */
    @FXML
    public void displayMessage(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatView.getItems().add(text);
            }
        });
    }


    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGameId(String gameid) {
        this.gameId = gameid;
    }




}
