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
    private int[] player0 = new int[6];
    private int[] player1 = new int[6];
    private int[] player2 = new int[6];
    private int[] player3 = new int[6];

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
     * sets the player names array, the first player gets to start first.
     */
    public Game(){
    }

    /**
     * @param players the name of the players in the game
     */
    public void addAllPlayers(User players[]) {
        this.players = players;
        numPlayers = 4;
    }

    /**
     */
    public boolean addOnePlayer(User player) {
        if (numPlayers <4) {
            this.players[numPlayers] = player;
            numPlayers++;
            synchronized (players) {
                for (User plyr: players)
                    try {
                        StringBuilder gameUsers = new StringBuilder("GAME USERS|" + id + "|" + name);
                        for (int i=0;i<numPlayers;i++) {
                            gameUsers.append("|");
                            gameUsers.append(players[i].getNickname());
                        }
                        plyr.write(gameUsers.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        playerLeft(player);
                        logger.log(Level.SEVERE, "an exception was thrown", e);
                    }
            }
            return true;
        }
        else
            return false;
    }

    /**
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
            player0[i] = 0;
            player1[i] = 0;
            player2[i] = 0;
            player3[i] = 0;
        }
        userPlaces.put(players[0].getNickname(), player0);
        userPlaces.put(players[1].getNickname(), player1);
        userPlaces.put(players[2].getNickname(), player2);
        userPlaces.put(players[3].getNickname(), player3);

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
     * sets the id for the game
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
     * starts a game, writes to all players that the game has started, also includes the name of the
     * players in the game. Then tells them who is the first player to play their turn.
     */
    public void startGame(Vector<User> closed) {
        synchronized (players) {
            for (User player : players)
                try {
                    StringBuilder gameUsers = new StringBuilder("GAME USERS|" + id + "|" + name);
                    for (int i=0;i<numPlayers;i++) {
                        gameUsers.append("|");
                        gameUsers.append(players[i].getNickname());
                    }
                    player.write(gameUsers.toString());
                    player.write("GAME|"+ id + "|" + name +"|TURN|"+playerTurn.getNickname());
                } catch (Exception e) {
                    closed.add(player);
                    e.printStackTrace();
                    playerLeft(player);
                    logger.log(Level.SEVERE, "an exception was thrown", e);
                }
        }
    }

    /**
     * starts a game, writes to all players that the game has started, also includes the name of the
     * players in the game. Then tells them who is the first player to play their turn.
     */
    public void gameCreated(Vector<User> closed) {
        synchronized (players) {
            for (User player : players)
                try {
                    player.write("GAME JOINED|" +id+ "|" +name);
                } catch (Exception e) {
                    closed.add(player);
                    e.printStackTrace();
                    playerLeft(player);
                    logger.log(Level.SEVERE, "an exception was thrown", e);
                }
        }
    }

    /**
     * handles all game commands and logic. Does different things depending on what type of command it is,
     * handles roll, move, and chat commands. also passes the turn when a player is done. Checks if a player has
     * won and alerts all players if thats the case.
     * @param cmd the command object containging the game command
     * @param usersClosedSocets the list of users who has closed sockets
     */
    public void gameHandler(Command cmd, Vector<User> usersClosedSocets) {
        if (cmd.getRawCmd().startsWith("GAME|" + id + "|" + name + "|ROLL")) {
            String roll;
            if (playerTurn == cmd.getUser()) {
                roll = rollDices();
                userPlaces.get(playerTurn.getNickname())[4] = Integer.parseInt(roll);
                userPlaces.get(playerTurn.getNickname())[5] += 1;
                synchronized (players) {
                    for (User player : players)
                        try {
                            player.write("GAME|" + id + "|" + name + "|ROLL|" + playerTurn.getNickname() + "|" + roll);
                        } catch (Exception e) {
                            usersClosedSocets.add(player);
                            playerLeft(player);
                            e.printStackTrace();
                            logger.log(Level.SEVERE, "an exception was thrown", e);
                        }
                }
            }
        }
        else if (cmd.getRawCmd().startsWith("GAME|" + id + "|" + name + "|MOVE")) {
            if (playerTurn == cmd.getUser()) {
                int playerSquare[] = userPlaces.get(playerTurn.getNickname());
                int pieceToMove = Integer.parseInt(cmd.getRawCmd().split("\\|")[4]);

                if ((playerSquare[pieceToMove] == 0) && (playerSquare[4] == 6)) {
                    playerSquare[pieceToMove] = 1;
                    checkMoveBackTostart(playerSquare, pieceToMove, usersClosedSocets);

                }
                else if (playerSquare[pieceToMove] != 0) {
                    if ((playerSquare[pieceToMove] + playerSquare[4]) > victorySquare) {
                        int overFlow = playerSquare[pieceToMove] + playerSquare[4];
                        overFlow = overFlow - victorySquare;
                        playerSquare[pieceToMove] = victorySquare - overFlow;
                        moveBack = true;
                        moveBackSteps = overFlow * -1;
                    }
                    else {
                        playerSquare[pieceToMove] += playerSquare[4];
                        checkMoveBackTostart(playerSquare, pieceToMove, usersClosedSocets);
                    }
                }

                int victory = 0;
                for (int i=0;i<4;i++) {
                    if (playerSquare[i] == (victorySquare)) {
                       victory += 1;
                    }
                }

                if (victory == 4) {
                    synchronized (players) {
                        for (User player : players)
                            try {
                                player.write("GAME|" + id + "|" + name + "|WIN|" + playerTurn.getNickname());
                            } catch (Exception e) {
                                usersClosedSocets.add(player);
                                playerLeft(player);
                                e.printStackTrace();
                                logger.log(Level.SEVERE, "an exception was thrown", e);
                            }
                    }
                }

                synchronized (players) {
                    for (User player : players)
                        try {
                            if (!moveBack)
                            player.write("GAME|" + id + "|" + name + "|MOVE|" + playerTurn.getNickname() + "|" +
                                    String.valueOf(pieceToMove) + "|" +
                                    userPlaces.get(playerTurn.getNickname())[pieceToMove]);
                            else {
                                player.write("GAME|" + id + "|" + name + "|MOVE|" + playerTurn.getNickname() + "|" +
                                        String.valueOf(pieceToMove) + "|" + moveBackSteps);
                            }
                        } catch (Exception e) {
                            usersClosedSocets.add(player);
                            playerLeft(player);
                            e.printStackTrace();
                            logger.log(Level.SEVERE, "an exception was thrown", e);
                        }
                }
                moveBack = false;

                if (playerSquare[4] != 6 && playerSquare[0] == 0 && playerSquare[1] == 0
                        && playerSquare[2] == 0 && playerSquare[3] == 0 && playerSquare[5] != 3 ){
                    synchronized (players) {
                        for (User player : players)
                            try {
                                player.write("GAME|" + id + "|" + name + "|TURN|" + playerTurn.getNickname());
                            } catch (Exception e) {
                                usersClosedSocets.add(player);
                                playerLeft(player);
                                e.printStackTrace();
                                logger.log(Level.SEVERE, "an exception was thrown", e);
                            }
                    }
                }
                else if (playerSquare[4] == 6 && playerSquare[5] != 3) {
                    synchronized (players) {
                        for (User player : players)
                            try {
                                player.write("GAME|" + id + "|" + name + "|TURN|" + playerTurn.getNickname());
                            } catch (Exception e) {
                                usersClosedSocets.add(player);
                                playerLeft(player);
                                e.printStackTrace();
                                logger.log(Level.SEVERE, "an exception was thrown", e);
                            }
                    }
                }
                else {
                    playerSquare[5] = 0;
                    passTurn(usersClosedSocets);
                }
            }
        }
        else if (cmd.getRawCmd().startsWith("GAME|" + id + "|" + name + "|CHAT")) {
            synchronized (players) {
                for (User player : players)
                    try {
                        player.write(cmd.getRawCmd());
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.log(Level.SEVERE, "an exception was thrown", e);
                    }
            }
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
     */
    private void passTurn(Vector<User> closed) {
        if (turnInt < numPlayers - 1)
            turnInt++;
        else
            turnInt = 0;
        playerTurn=players[turnInt];

        synchronized (players) {
            for (User player : players)
                try {
                    player.write("GAME|"+ id + "|" + name +"|TURN|"+playerTurn.getNickname());
                } catch (Exception e) {
                    closed.add(player);
                    e.printStackTrace();
                    playerLeft(player);
                    logger.log(Level.SEVERE, "an exception was thrown", e);
                }
        }
    }

    private void checkMoveBackTostart(int playerSquare[], int pieceToMove, Vector<User> closedSockets) {
            for (int i=0;i<4;i++) {
                for (int j=0;j<4;j++) {
                    if (! players[i].getNickname().equals(playerTurn.getNickname())) {
                        int pieces[] = userPlaces.get(players[i].getNickname());
                        if (pieces[j] < 53 && playerSquare[pieceToMove] < 53) {
                            if (realBoardMap[turnInt][playerSquare[pieceToMove]] == realBoardMap[i][pieces[j]]) {
                                System.out.println("player moved to: " + playerSquare[pieceToMove] +
                                        " : " + realBoardMap[turnInt][playerSquare[pieceToMove]] + " already : "
                                        + pieces[j] + " : " + realBoardMap[i][pieces[j]]);
                                synchronized (players) {
                                    for (User player : players)
                                        try {
                                            player.write("GAME|" + id + "|" + name + "|MOVE|" +
                                                    players[i].getNickname() + "|" + j + "|" + 0);
                                        } catch (Exception e) {
                                            playerLeft(player);
                                            closedSockets.add(player);
                                            e.printStackTrace();
                                            logger.log(Level.SEVERE, "an exception was thrown", e);
                                        }
                                }
                                userPlaces.get(players[i].getNickname())[j] = 0;
                            }
                        }
                    }
                }
            }
    }

    private void playerLeft(User player) {
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
            synchronized (players) {
                for (User user : players) {
                    try {
                        user.write("GAME|" + id + "|" + name + "|LEFT|" + player.getNickname());
                    } catch (Exception e) {
                        e.printStackTrace();
                        playerLeft(user);
                        logger.log(Level.SEVERE, "an exception was thrown", e);
                    }
                }
            }
        }
    }

}
