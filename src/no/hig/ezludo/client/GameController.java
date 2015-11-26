package no.hig.ezludo.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

/**
 * @author Kristian
 * date 12.11.2015.
 * This class contains all game-mechnaism done on the client.
 */
public class GameController {
    private static Logger logger = Logger.getAnonymousLogger();

    // Constant: Defines the sleepingtime for diceroll and piecemove
    final static private int movePieceLag = 500;
    final static private int diceLag = 100;


    //Game-information varibales
    String gameName;
    String gameId;


    //Ludo board
    LudoBoardCoordinates ludoBoardCoordinates;
    @FXML private ImageView ludoBoardImage;
    Image board;
    private HashMap<Integer, EventHandler<MouseEvent>> events = new HashMap<>();

     // Buttons
    @FXML Button inviteButton;
    @FXML Button startGame;


    // Player
    @FXML TextField label;
    HashMap<String, String> players = new HashMap<>();
    String myNickname;
    boolean yourTurn = false;


    //Chat
    @FXML ListView chatView;

    // Dices
    @FXML private ImageView diceImage;
    private Image dice;
    private int diceNr;
    private static final int diceMin = 1;
    private static final int diceMax = 6;
    private static final int rounds = 8;


    // Ludo piece imageview arrays
    @FXML private ImageView redPieces[];
    @FXML private ImageView bluePieces[];
    @FXML private ImageView yellowPieces[];
    @FXML private ImageView greenPieces[];


    //Image of ludo pieces
    private Image redImage[];
    private Image yellowImage[];
    private Image blueImage[];
    private Image greenImage[];


   //ImageView for each ludo-piece
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


     // Controller mechanisms
     // Boolean variabels to control where the pieces are
    private boolean[] redInStart;
    private boolean[] blueInStart;
    private boolean[] yellowInStart;
    private boolean[] greenInStart;

    private boolean[] redInGoal;
    private boolean[] blueInGoal;
    private boolean[] yellowInGoal;
    private boolean[] greenInGoal;

    //Used if a player has to move back from goal
    private boolean moveBack;



    // Array for each piece.
    // Controls the position of each pice with a single number
    // that equals a square in the ludo board

    private int[] redCurrent;
    private int[] blueCurrent;
    private int[] greenCurrent;
    private int[] yellowCurrent;


    /**
     * Ludoboard constructor.
     * Setup the board board and the dice.
     * Also sets all necessary variables which is defined above
     * */
    public void ludoBoard() {

        //Set the ludo board and the dice
        ludoBoardCoordinates = new LudoBoardCoordinates();
        board = new Image("/res/board.png");
        dice = new Image("/res/dices/dice1.png");
        this.ludoBoardImage.setImage(board);
        this.diceImage.setImage(dice);

        //Create image views the pieces
        redPieces = new ImageView[4];
        bluePieces = new ImageView[4];
        yellowPieces = new ImageView[4];
        greenPieces = new ImageView[4];

        //Creates images for each piece
        redImage = new Image[4];
        yellowImage = new Image[4];
        blueImage = new Image[4];
        greenImage =  new Image[4];

        //Creates boolean variables for control mechanisms
        redInStart = new boolean[4];
        blueInStart = new boolean[4];
        yellowInStart = new boolean[4];
        greenInStart = new boolean[4];

        redInGoal = new boolean[4];
        blueInGoal = new boolean[4];
        yellowInGoal = new boolean[4];
        greenInGoal = new boolean[4];

        //Creates the placeholder variables
        redCurrent = new int[4];
        blueCurrent = new int[4];
        greenCurrent = new int[4];
        yellowCurrent = new int[4];

        //Set all imageviews
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

        //Sets all images of pieces
        for (int i=0;i<4;i++) {
            redImage[i] = new Image("/res/red2final.png");
            blueImage[i] = new Image("/res/blue2final.png");
            greenImage[i] = new Image("/res/green2final.png");
            yellowImage[i] = new Image("/res/yellow2final.png");
        }
        setupBoard();
    }

    /**
     * Adds all pieces to the ludo board and initialize all controller mechanisms.
     * Sets all pices in start area with position 0 and bolean Start as true.
     * Boolean in goal as False and moveback as false.
     * */
    public void setupBoard(){
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
        moveBack = false;
    }

    /**
     * Assign each player to a color.
     * @param players The players names
     * @param nickname Alias of the player
     *
     * */
    public void setupPlayers(String[] players, String nickname) {
        String[] colors = new String[4];
        colors[0] = "red";
        colors[1] = "blue";
        colors[2] = "yellow";
        colors[3] = "green";


        //Add the players and their colors to the players hash map.
        for (int i = 0; i < players.length; i++) {
            if (players[i] != (null))
                this.players.put(players[i], colors[i]);
        }

        // Adding the player's nickname and type of color to the screen
        myNickname = nickname;
        label.setText("\t\t "+Login.getTranslation().getString("red")+": " + players[0] + ",\t\t "+Login.getTranslation().getString("blue")+": " + players[1] +
                ",\t\t "+Login.getTranslation().getString("yellow")+": " + players[2] + ",\t\t "+Login.getTranslation().getString("green")+": " + players[3]);
    }

    /**
     * TODO
     * @param command Player data.
     * command[4] is the players name.
     * command[5] is how many moves \ random int as dice
     * */
    @FXML
    public void playerRoll(String[] command) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatView.getItems().add(command[4] + "\t" + Login.getTranslation().getString("rolled") + "\t" + command[5]);
            }
        });
        if (command[4].equals(myNickname)) {
            showDices(command[5]);
            ImageView pieceArray[];
            int colorCurrent[];
            if (("red").equals(players.get(myNickname))) {
                pieceArray = redPieces;
                colorCurrent = redCurrent;
            }
            else if (("blue").equals(players.get(myNickname))) {
                pieceArray = bluePieces;
                colorCurrent = blueCurrent;
            }
            else if (("yellow").equals(players.get(myNickname))) {
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
                if (colorCurrent[i] != 0 || ("6").equals(command[5])) {
                    allInStart = false;
                    EventHandler<MouseEvent> event;
                    pieceArray[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event = new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            MainController.getClient().movePiece(gameId, gameName, String.valueOf(pieceToMove));
                            movedPiece(pieceArray);
                        }
                    });
                    events.put(i, event);
                }
            }
            if (allInStart)
                MainController.getClient().movePiece(gameId, gameName, "0");
        }
        else {
            showDices(command[5]);
        }
    }

    /**
     * Removes mouseevents after players turn
     * @param array Array of pieces
     **/
    public void movedPiece(ImageView[] array) {
        for (int i=0;i<4;i++) {
            if (events.containsKey(i)) {
                array[i].removeEventHandler(MouseEvent.MOUSE_CLICKED, events.get(i));
            }
        }
        events.clear();
    }

    /**
     * Textbox dialog for inviting a player
     **/
    @FXML
    public void invitePlayer() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(Login.getTranslation().getString("invitePlayer"));
        dialog.setHeaderText(Login.getTranslation().getString("inviteByName"));
        dialog.setContentText(Login.getTranslation().getString("inviteName"));

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> MainController.getClient().sendGameInvite(name, gameId));
    }

    /**
     * //TODO
     * @param command Player data from server
     **/
    @FXML
    public void playerTurn(String[] command) {
        if (inviteButton.isVisible())
            inviteButton.setVisible(false);

        if (startGame.isVisible())
            startGame.setVisible(false);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                displayMessage(command[4] + Login.getTranslation().getString("turn"));
            }
        });
        if (command[4].equals(myNickname)) {
            yourTurn = true;
        }
    }

    /**
     * //TODO
     * @param command Players data from server
     * command[4] is the players name.
     * command[5] is how many moves \ random int as dice
     * command[6] //TODO
     **/
    public void playerMove(String[] command) {
        if (("red").equals(players.get(command[4])) && !redInGoal[Integer.parseInt(command[5])]) {
           movePiece(command[6], redCurrent[Integer.parseInt(command[5])],
                   redPieces[Integer.parseInt(command[5])], redImage[Integer.parseInt(command[5])],
                   Integer.parseInt(command[5]), ludoBoardCoordinates.redFinish, "red");
        }

        else if (("blue").equals(players.get(command[4])) && !blueInGoal[Integer.parseInt(command[5])]) {
            movePiece(command[6], blueCurrent[Integer.parseInt(command[5])],
                    bluePieces[Integer.parseInt(command[5])], blueImage[Integer.parseInt(command[5])],
                    Integer.parseInt(command[5]), ludoBoardCoordinates.blueFinish, "blue");
        }

        else if (("yellow").equals(players.get(command[4])) && !yellowInGoal[Integer.parseInt(command[5])]) {
            movePiece(command[6], yellowCurrent[Integer.parseInt(command[5])],
                    yellowPieces[Integer.parseInt(command[5])], yellowImage[Integer.parseInt(command[5])],
                    Integer.parseInt(command[5]), ludoBoardCoordinates.yellowFinish, "yellow");
        }

        else if (("green").equals(players.get(command[4])) && !greenInGoal[Integer.parseInt(command[5])]) {
            movePiece(command[6], greenCurrent[Integer.parseInt(command[5])],
                    greenPieces[Integer.parseInt(command[5])], greenImage[Integer.parseInt(command[5])],
                    Integer.parseInt(command[5]), ludoBoardCoordinates.greenFinish, "green");
        }


    }


    public void movePiece(String moves, int colorCurrent, ImageView imageView, Image image,
                         int piece, double[][] finishArray, String color) {
        Thread moveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int colCurrent = colorCurrent;
                int squareInArray = 0;
                if (("red").equals(color)) {
                    squareInArray = colCurrent;
                }
                else if (("blue").equals(color)) {
                    squareInArray = colCurrent+13;
                }
                else if (("green").equals(color)) {
                    squareInArray = colCurrent+39;
                }
                else if (("yellow").equals(color)) {
                    squareInArray = colCurrent+ 26;
                }

                System.out.println(moves + ":" + Integer.parseInt(moves));
                int stop;
                if (Integer.parseInt(moves) > 0)
                    stop = Integer.parseInt(moves);
                else if (Integer.parseInt(moves) == 0) {
                    if (("red").equals(color)) {
                       redPieces[piece].setX(ludoBoardCoordinates.redStart[piece + 1][1] * 600);
                       redPieces[piece].setY(ludoBoardCoordinates.redStart[piece + 1][2] * 600);
                       redPieces[piece].setImage(redImage[piece]);
                       redCurrent[piece] = 0;
                    }
                    else if (("blue").equals(color)) {
                        bluePieces[piece].setX(ludoBoardCoordinates.blueStart[piece + 1][1] * 600);
                        bluePieces[piece].setY(ludoBoardCoordinates.blueStart[piece + 1][2] * 600);
                        bluePieces[piece].setImage(blueImage[piece]);
                        blueCurrent[piece] = 0;
                    }
                    else if (("green").equals(color)) {
                        greenPieces[piece].setX(ludoBoardCoordinates.greenStart[piece + 1][1] * 600);
                        greenPieces[piece].setY(ludoBoardCoordinates.greenStart[piece + 1][2] * 600);
                        greenPieces[piece].setImage(greenImage[piece]);
                        greenCurrent[piece] = 0;
                    }
                   else  if (("yellow").equals(color)) {
                        yellowPieces[piece].setX(ludoBoardCoordinates.yellowStart[piece + 1][1] * 600);
                        yellowPieces[piece].setY(ludoBoardCoordinates.yellowStart[piece + 1][2] * 600);
                        yellowPieces[piece].setImage(yellowImage[piece]);
                        yellowCurrent[piece] = 0;
                   }
                    stop = 0;
                    System.out.println("back to start");
                }
                else {
                    moveBack = true;
                    stop = 59;
                    System.out.print("moveback true");
                }
                while (colCurrent+1 <= stop) {
                    colCurrent++;
                    squareInArray++;

                    if(colCurrent== 53) {
                        if (("red").equals(color)) {
                            imageView.setX(ludoBoardCoordinates.mainArea[1][1] * 600);
                            imageView.setY(ludoBoardCoordinates.mainArea[1][2] * 600);
                        }
                        else if (("blue").equals(color)) {
                            imageView.setX(ludoBoardCoordinates.mainArea[14][1] * 600);
                            imageView.setY(ludoBoardCoordinates.mainArea[14][2] * 600);
                        }
                        else if (("green").equals(color)) {
                            imageView.setX(ludoBoardCoordinates.mainArea[40][1] * 600);
                            imageView.setY(ludoBoardCoordinates.mainArea[40][2] * 600);
                        }
                        else if (("yellow").equals(color)) {
                            imageView.setX(ludoBoardCoordinates.mainArea[27][1] * 600);
                            imageView.setY(ludoBoardCoordinates.mainArea[27][2] * 600);
                        }
                        imageView.setImage(image);
                    }

                    else if(colCurrent > 53 && colCurrent< 59) {
                        imageView.setX(finishArray[colCurrent - 53][1] * 600);
                        imageView.setY(finishArray[colCurrent - 53][2] * 600);
                        imageView.setImage(image);
                    }

                    else if (colCurrent == 59 && stop == 59) {
                        imageView.setX(finishArray[6][1] * 600);
                        imageView.setY(finishArray[6][2] * 600);
                        imageView.setImage(image);


                        if (!moveBack) {
                            if (("red").equals(color)) {
                                redInGoal[piece] = true;
                            } else if (("blue").equals(color)) {
                                blueInGoal[piece] = true;
                            } else if (("green").equals(color)) {
                                greenInGoal[piece] = true;
                            } else if (("yellow").equals(color)) {
                                yellowInGoal[piece] = true;
                            }
                            System.out.print("ongoal");
                        }

                    }
                    else if(colCurrent < 53) {
                        // Flytter vanlig i main
                        if (("red").equals(color)) {
                                imageView.setX(ludoBoardCoordinates.mainArea[squareInArray][1] * 600);
                                imageView.setY(ludoBoardCoordinates.mainArea[squareInArray][2] * 600);
                        }
                        else if (("blue").equals(color)) {
                            if (squareInArray < 53) {
                                imageView.setX(ludoBoardCoordinates.mainArea[squareInArray][1] * 600);
                                imageView.setY(ludoBoardCoordinates.mainArea[squareInArray][2] * 600);
                            }
                            else {
                                imageView.setX(ludoBoardCoordinates.mainArea[squareInArray-52][1] * 600);
                                imageView.setY(ludoBoardCoordinates.mainArea[squareInArray-52][2] * 600);
                            }
                        }
                        else if (("green").equals(color)) {
                            if (squareInArray < 53) {
                                imageView.setX(ludoBoardCoordinates.mainArea[squareInArray][1] * 600);
                                imageView.setY(ludoBoardCoordinates.mainArea[squareInArray][2] * 600);
                            }
                            else {
                                imageView.setX(ludoBoardCoordinates.mainArea[squareInArray-52][1] * 600);
                                imageView.setY(ludoBoardCoordinates.mainArea[squareInArray-52][2] * 600);
                            }
                        }
                        else if (("yellow").equals(color)) {

                            if (squareInArray < 53) {
                                imageView.setX(ludoBoardCoordinates.mainArea[squareInArray][1] * 600);
                                imageView.setY(ludoBoardCoordinates.mainArea[squareInArray][2] * 600);
                            }
                            else {
                                imageView.setX(ludoBoardCoordinates.mainArea[squareInArray-52][1] * 600);
                                imageView.setY(ludoBoardCoordinates.mainArea[squareInArray-52][2] * 600);
                            }
                        }
                        imageView.setImage(image);
                    }
                    try {
                        Thread.sleep(movePieceLag);
                    } catch (InterruptedException e) {
                        logger.log(Level.SEVERE, "an exception was thrown", e);
                    }
                }
                if(moveBack) {
                    moveBack(stop+Integer.parseInt(moves), finishArray, color, imageView, image, piece, colCurrent);
                    System.out.print("moveBack() called");
                    System.out.print("\n");
                    moveBack = false;
                }
                else {
                    System.out.println("updated current");
                    if (("red").equals(color)) {
                        redCurrent[piece] = colCurrent;
                        System.out.println("updated red" + redCurrent[piece]+":"+colCurrent);
                    }
                    else if (("blue").equals(color)) {
                        blueCurrent[piece] = colCurrent;
                        System.out.println("updated blue " + blueCurrent[piece]+":"+colCurrent);
                    }
                    else if (("yellow").equals(color)) {
                        yellowCurrent[piece] = colCurrent;
                        System.out.println("updated yellow" + yellowCurrent[piece]+":"+colCurrent);
                    }
                    else if (("green").equals(color)) {
                        greenCurrent[piece] = colCurrent;
                        System.out.println("updated green " + greenCurrent[piece]+":"+colCurrent);
                    }
                }
            }
        });
        moveThread.start();
    }

    public void moveBack(int nr, double[][] finish, String color, ImageView imageView, Image image,
                         int piece, int colCurrent) {
        Thread movingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.print("is in goal, moving back to: " + String.valueOf(nr));
                int i = 6;
                int totalSteps = 59;
                while (totalSteps != nr) {
                    totalSteps--;
                    i--;
                    imageView.setX(finish[i][1] * 600);
                    imageView.setY(finish[i][2] * 600);
                    imageView.setImage(image);
                    try {
                        Thread.sleep(movePieceLag);
                    } catch (InterruptedException e) {
                        logger.log(Level.SEVERE, "an exception was thrown", e);
                    }
                }
                if (("red").equals(color)) {
                    redCurrent[piece] = nr;
                }
                else if (("blue").equals(color)) {
                    blueCurrent[piece] = nr;
                }
                else if (("yellow").equals(color)) {
                    yellowCurrent[piece] = nr;
                }
                else if (("green").equals(color)) {
                    greenCurrent[piece] = nr;
                }
            }
        });
        movingThread.start();
    }

    public void rollDices() {
        if (yourTurn) {
            MainController.getClient().rollDice(gameId, gameName);
            yourTurn = false;
        }
    }

    public void showDices(String nrToShow)  {
        Thread diceLoadingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i<=rounds; i++) {
                    diceNr = randomInt(diceMin, diceMax);
                    showImage(diceNr);
                    try {
                        Thread.sleep(diceLag);
                    } catch (InterruptedException e) {
                        logger.log(Level.SEVERE, "an exception was thrown", e);
                    }
                }
                dice = new Image("/res/dices/dice" + Integer.parseInt(nrToShow) +".png");
                diceImage.setImage(dice);
            }
        });
        diceLoadingThread.start();
    }

    public static int randomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;

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
        MainController.getClient().sendGameMessage(source.getText(), gameId, gameName);
        source.clear();
    }

    /**
     * This method receives a message, and displays it in the game chat.
     * @param text The message
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

    public void startGame() {
        MainController.getClient().startGame(gameId);
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGameId(String gameid) {
        this.gameId = gameid;
    }




}
