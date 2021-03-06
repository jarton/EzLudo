package no.hig.ezludo.server;

import no.hig.ezludo.server.commands.Command;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class handles all game commands and has all info about one game, and uses game logic to tell the clients
 * what to do and the state of the game.
 */
public class Game {
    private User players[];

    // the square the different pieces to a player is in. the 5 int is the last roll of that player
    private int[][] playerSquares = new int[4][6];

    // maps the usernames to the placement array of the users
    private HashMap<String, int[]> userPlaces = new HashMap<>();
    private String name = "Random Game";
    private User playerTurn;
    private int id = -1;
    private final int diceMin = 1;
    private final int diceMax = 6;
    private final int victorySquare = 59;
    private int turnInt;
    private int numPlayers = 0;
    private boolean moveBack = false;
    private int moveBackSteps = 0;
    private int realBoardMap [][] = new int[4][53];
    private static Logger logger = Logger.getAnonymousLogger();

    /**
     * constructor creates a new user array to be used later.
     */
    public Game(){
        players = new User[4];
    }

    /**
     * adds all players to the game at once. This is used for random games
     * where all the users are added in one go.
     * @param players the name of the players in the game
     */
    public void addAllPlayers(User players[]) {
        this.players = players;
        numPlayers = 4;
    }

    /**
     * adds one player to the game and updates the count of players in the game.
     * Then it writes to all players currently in the game what the names of the
     * players are so they can see when a new player is added.
     * @param player the player to add
     * @param closed list of users to be removed from the server
     * @return true if the player was added, false if not.
     */
    public boolean addOnePlayer(User player, Vector<User> closed) {
        if (numPlayers < 4) {
            this.players[numPlayers] = player;
            numPlayers++;
                for (int i = 0;i<numPlayers;i++) {
                    try {
                        StringBuilder gameUsers = new StringBuilder("GAME USERS|" + id + "|" + name);
                        for (int j=0;j<numPlayers;j++) {
                            gameUsers.append("|");
                            gameUsers.append(players[j].getNickname());
                        }
                        players[i].write(gameUsers.toString());
                    } catch (Exception e) {
                        playerLeft(player, closed);
                        logger.log(Level.SEVERE, "an exception was thrown", e);
                    }
            }
            return true;
        }
        else
            return false;
    }

    /**
     * sets up the game. the turnint that contains the index of the player in the array whose turn it is.
     * sets the playerturn to point to the player who's first. and calls initPlaces to set up the placement.
     */
    public void setUpGame() {
        turnInt = 0;
        playerTurn = players[turnInt];
        initPlaces();
    }

    /**
     * zeroes out the player placement array and fills the hashmap with the names and arrays.
     */
    private void initPlaces() {
        for (int i = 0; i < 6; i++) {
            playerSquares[0][i] = 0;
            playerSquares[1][i] = 0;
            playerSquares[2][i] = 0;
            playerSquares[3][i] = 0;
        }

        for (int i=0;i<numPlayers;i++) {
            userPlaces.put(players[i].getNickname(), playerSquares[i]);
        }


        int j = 1;
        for (int i=1;i<53;i++) {
            realBoardMap[0][i] = j;
            j++;
        }

        j = 14;
        for (int i=1;i<53;i++) {
            if (j ==53)
               j = 1;
            realBoardMap[1][i] = j;
            j++;
        }

        j = 27;
        for (int i=1;i<53;i++) {
            if (j ==53)
                j = 1;
            realBoardMap[2][i] = j;
            j++;
        }

        j = 40;
        for (int i=1;i<53;i++) {
            if (j ==53)
                j = 1;
            realBoardMap[3][i] = j;
            j++;
        }
    }

    /**
     * sets the name of the game.
     * @param name the name to set to the game
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets the id for the game
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *  gets the name of the game
     * @return the name String
     */
    public String getName() {
        return name;
    }

    /**
     * starts the game, writes all the player names to all players. Then sends out the message
     * that tells the players who is first, this enables the game to go on.
     * @param closed the list of the users to remove from the server
     */
    public void startGame(Vector<User> closed) {
        for (int i=0;i<numPlayers;i++) {
                try {
                    StringBuilder gameUsers = new StringBuilder("GAME USERS|" + id + "|" + name);
                    for (int j=0;j<numPlayers;j++) {
                        gameUsers.append("|");
                        gameUsers.append(players[j].getNickname());
                    }
                    players[i].write(gameUsers.toString());
                    players[i].write("GAME|"+ id + "|" + name +"|TURN|"+playerTurn.getNickname());
                } catch (Exception e) {
                    closed.add(players[i]);
                    playerLeft(players[i], closed);
                    logger.log(Level.SEVERE, "an exception was thrown", e);
                }
        }
    }

    /**
     * This function is used to make the clients spawn a tab with the game ui.
     * If its a random game this happens for all players. If its a premade game
     * this happens only for the host. The rest of the game joined commands
     * gets sendt if a user gets invited and accepts.
     * @param closed the list of users to be removed from the server
     */
    public void gameCreated(Vector<User> closed) {
        for (int i=0;i<numPlayers;i++) {
                try {
                    players[i].write("GAME JOINED|" +id+ "|" +name);
                } catch (Exception e) {
                    closed.add(players[i]);
                    playerLeft(players[i], closed);
                    logger.log(Level.SEVERE, "an exception was thrown", e);
                }
        }
    }

    /**
     * handles all game commands and logic. Does different things depending on what type of command it is,
     * handles roll, move, and chat commands. also passes the turn when a player is done. All this is done
     * by writing to the users in the game what other players are doing, what to do next, and the state of the game.
     * Checks if a player has won and alerts all players if thats the case.
     * Function gets called from the commandHandler if it handles a GameCommand object.
     * @param cmd the command object containging the game command
     * @param usersClosedSocets the list of users who has closed sockets
     */
    public void gameHandler(Command cmd, Vector<User> usersClosedSocets) {
        // rolls the dice and lets all players know what the roll was. saves the roll in that users array.
        if (cmd.getRawCmd().startsWith("GAME|" + id + "|" + name + "|ROLL")) {
            String roll;
            if (playerTurn == cmd.getUser()) {
                roll = rollDices();
                userPlaces.get(playerTurn.getNickname())[4] = Integer.parseInt(roll);
                userPlaces.get(playerTurn.getNickname())[5] += 1;
                    for (int i=0;i<numPlayers;i++) {
                        try {
                            players[i].write("GAME|" + id + "|" + name + "|ROLL|" + playerTurn.getNickname() + "|" + roll);
                        } catch (Exception e) {
                            usersClosedSocets.add(players[i]);
                            playerLeft(players[i], usersClosedSocets);
                            logger.log(Level.SEVERE, "an exception was thrown", e);
                        }
                }
            }
        }
        else if (cmd.getRawCmd().startsWith("GAME|" + id + "|" + name + "|MOVE")) {
            if (playerTurn == cmd.getUser()) {
                int playerSquare[] = userPlaces.get(playerTurn.getNickname());
                int pieceToMove = Integer.parseInt(cmd.getRawCmd().split("\\|")[4]);

                // if player in start and rolls 6 move to square 1
                if ((playerSquare[pieceToMove] == 0) && (playerSquare[4] == 6)) {
                    playerSquare[pieceToMove] = 1;
                    checkMoveBackTostart(playerSquare, pieceToMove, usersClosedSocets);

                }
                // if player not in start
                else if (playerSquare[pieceToMove] != 0) {
                    // if move will push player beyond the goal square, moveback = true;
                    if ((playerSquare[pieceToMove] + playerSquare[4]) > victorySquare) {
                        int overFlow = playerSquare[pieceToMove] + playerSquare[4];
                        overFlow = overFlow - victorySquare;
                        playerSquare[pieceToMove] = victorySquare - overFlow;
                        moveBack = true;
                        moveBackSteps = overFlow * -1;
                    }
                    // move normally
                    else {
                        playerSquare[pieceToMove] += playerSquare[4];
                        checkMoveBackTostart(playerSquare, pieceToMove, usersClosedSocets);
                    }
                }

                // checks for win condition
                int victory = 0;
                for (int i=0;i<4;i++) {
                    if (playerSquare[i] == (victorySquare)) {
                       victory += 1;
                    }
                }

                // if player won alert all players
                if (victory == 4) {
                        for (int i=0;i<numPlayers;i++) {
                            try {
                                players[i].write("GAME|" + id + "|" + name + "|WIN|" + playerTurn.getNickname());
                            } catch (Exception e) {
                                usersClosedSocets.add(players[i]);
                                playerLeft(players[i], usersClosedSocets);
                                logger.log(Level.SEVERE, "an exception was thrown", e);
                            }
                    }
                }
                // sends movement of piece to all players, if moveback send negative int, gameController handles that.
                for (int i=0;i<numPlayers;i++) {
                   try {
                       if (!moveBack)
                       players[i].write("GAME|" + id + "|" + name + "|MOVE|" + playerTurn.getNickname() + "|" +
                               String.valueOf(pieceToMove) + "|" +
                               userPlaces.get(playerTurn.getNickname())[pieceToMove]);
                       else {
                           players[i].write("GAME|" + id + "|" + name + "|MOVE|" + playerTurn.getNickname() + "|" +
                                   String.valueOf(pieceToMove) + "|" + moveBackSteps);
                       }
                   } catch (Exception e) {
                       usersClosedSocets.add(players[i]);
                       playerLeft(players[i], usersClosedSocets);
                       logger.log(Level.SEVERE, "an exception was thrown", e);
                   }
                }
                moveBack = false;

                // if not rolled 6 and all pieces in start, and rolled less than 3 times in row get another roll
                if (playerSquare[4] != 6 && playerSquare[0] == 0 && playerSquare[1] == 0
                        && playerSquare[2] == 0 && playerSquare[3] == 0 && playerSquare[5] != 3 ){
                        for (int i=0;i<numPlayers;i++) {
                            try {
                                players[i].write("GAME|" + id + "|" + name + "|TURN|" + playerTurn.getNickname());
                            } catch (Exception e) {
                                usersClosedSocets.add(players[i]);
                                playerLeft(players[i], usersClosedSocets);
                                logger.log(Level.SEVERE, "an exception was thrown", e);
                            }
                    }
                }
                // if rolled 6 and rolled less then 3 times in row get anther roll.
                else if (playerSquare[4] == 6 && playerSquare[5] != 3) {
                        for (int i=0;i<numPlayers;i++) {
                            try {
                                players[i].write("GAME|" + id + "|" + name + "|TURN|" + playerTurn.getNickname());
                            } catch (Exception e) {
                                usersClosedSocets.add(players[i]);
                                playerLeft(players[i], usersClosedSocets);
                                logger.log(Level.SEVERE, "an exception was thrown", e);
                            }
                    }
                }
                // else put times rolled in row = 0 and pass turn to next player
                else {
                    playerSquare[5] = 0;
                    passTurn(usersClosedSocets);
                }
            }
        }
        // writes chat to all players in game
        else if (cmd.getRawCmd().startsWith("GAME|" + id + "|" + name + "|CHAT")) {
                for (int i=0;i<numPlayers;i++) {
                    try {
                        players[i].write(cmd.getRawCmd());
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "an exception was thrown", e);
                    }
            }
        }
        // leave command. remove player
        else if (cmd.getRawCmd().startsWith("GAME|" + id + "|" + name + "|LEAVE")) {
            playerLeft(cmd.getUser(), usersClosedSocets);
        }
    }

    /**
     *  gets a random number and returns it
     * @return string containing the roll int
     */
    public String rollDices() {
            int diceNr = randomInt(diceMin, diceMax);
        return String.valueOf(diceNr);
    }


    /**
     * gets a random generated int
     * @param min minimum int to get
     * @param max maximum int to get
     * @return the random int generated
     */
    private int randomInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    /**
     * passes the turn to the next player. Goes in the array of players from 0->4 and then back to 0.
     * Writes to all players whos turn it is after it has changed.
     * @param closed the list of users with closed sockets to add player to.
     */
    private void passTurn(Vector<User> closed) {
        if (turnInt < numPlayers - 1)
            turnInt++;
        else
            turnInt = 0;
        playerTurn=players[turnInt];

            for (int i=0;i<numPlayers;i++) {
                try {
                    players[i].write("GAME|"+ id + "|" + name +"|TURN|"+playerTurn.getNickname());
                } catch (Exception e) {
                    closed.add(players[i]);
                    playerLeft(players[i], closed);
                    logger.log(Level.SEVERE, "an exception was thrown", e);
                }
        }
    }

    /**
     * Checks if the player who just moved a piece landed on another players piece.
     * If thats the case the player who the current player landed on will go have his or her
     * piece go back to start. Game only knows how many moves the players has done. realBoardMap
     * maps blue 1 to red's 14 ect. so we can see if they are one the same place in the board but not logically.
     * @param playerSquare the array with the location of the current players pieces.
     * @param pieceToMove the int value 0-3 representing one of the 4 pieces.
     * @param closedSockets the list of the users to remove from the server.
     */
    private void checkMoveBackTostart(int playerSquare[], int pieceToMove, Vector<User> closedSockets) {
            for (int i=0;i<numPlayers;i++) {
                for (int j=0;j<4;j++) {
                    if (! players[i].getNickname().equals(playerTurn.getNickname())) {
                        int pieces[] = userPlaces.get(players[i].getNickname());
                        if (pieces[j] < 53 && playerSquare[pieceToMove] < 53) {
                            if (realBoardMap[turnInt][playerSquare[pieceToMove]] == realBoardMap[i][pieces[j]]) {
                                    // writes move command to update players game ui.
                                    for (int k=0;k<numPlayers;k++) {
                                        try {
                                            players[k].write("GAME|" + id + "|" + name + "|MOVE|" +
                                                    players[i].getNickname() + "|" + j + "|" + 0);
                                        } catch (Exception e) {
                                            playerLeft(players[k], closedSockets);
                                            closedSockets.add(players[k]);
                                            logger.log(Level.SEVERE, "an exception was thrown", e);
                                        }
                                }
                                //update location of piece that was knocked back
                                userPlaces.get(players[i].getNickname())[j] = 0;
                            }
                        }
                    }
                }
            }
    }

    /**
     * Function removes a player from the game and makes changes so the game can continue on.
     * It looks through the user array for the game and if it can find the player it moves all
     * players after it in the array one step back, updates the count of players.
     * If the player that left was the one that had the turn, the turn gets passed to the next player.
     * Then writes too all remaining players that a user left the game.
     * @param player User object of the player that left.
     * @param closed the list of users with closed sockets.
     */
    private void playerLeft(User player, Vector<User> closed) {
        int playerIndex = -1;
        for (int i = 0; i < numPlayers; i++) {
            if (players[i] == player) {
                playerIndex = i;
            }
        }
        if (playerIndex != -1) {
            int j = playerIndex;
            for (int i = playerIndex + 1; i < numPlayers; i++) {
                players[j] = players[i];
            }
            numPlayers--;
            if (playerIndex == turnInt) {
                passTurn(closed);
            }
                for (int i=0;i<numPlayers;i++) {
                    try {
                        players[i].write("GAME|" + id + "|" + name + "|CHAT||" +
                                player.getNickname() + " Left the game\n" +
                                "write something in chat to resume");
                    } catch (Exception e) {
                        playerLeft(players[i], closed);
                        logger.log(Level.SEVERE, "an exception was thrown", e);
                    }
                }
        }
    }

}
