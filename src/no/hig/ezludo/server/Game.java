package no.hig.ezludo.server;

import no.hig.ezludo.server.commands.Command;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

/**
 * class handles all game commands and has all info about one game, and uses game logic to tell the clients
 * what to do and the state of the game.
 */
public class Game {
    private User players[] = new User[4];

    // the square the different pieces to a player is in. the 5 int is the last roll of that player
    private int[] player0 = new int[5];
    private int[] player1 = new int[5];
    private int[] player2 = new int[5];
    private int[] player3 = new int[5];

    // maps the usernames to the placement array of the users
    private HashMap<String, int[]> userPlaces = new HashMap<>();
    private String name = "Random Game";
    private User playerTurn;
    private int id = -1;
    private final int diceMin = 1;
    private final int diceMax = 6;
    private final int victorySquare = 59;
    private int turnInt;
    private boolean moveBack = false;
    private int moveBackSteps = 0;

    /**
     * sets the player names array, the first player gets to start first.
     * @param players the name of the players in the game
     */
    public Game(User players[]) {
        this.players = players;
        turnInt = 0;
        playerTurn = players[turnInt];
        initPlaces();
    }

    /**
     * zeroes out the player placement array and fills the hashmap with the names and arrays.
     */
    private void initPlaces() {
        for (int i = 0; i < 4; i++) {
            player0[i] = 0;
            player1[i] = 0;
            player2[i] = 0;
            player3[i] = 0;
        }
        userPlaces.put(players[0].getNickname(), player0);
        userPlaces.put(players[1].getNickname(), player1);
        userPlaces.put(players[2].getNickname(), player2);
        userPlaces.put(players[3].getNickname(), player3);
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
    public void startGame() {
        synchronized (players) {
            for (User player : players)
                try {
                    player.write("GAME STARTED|" +id+ "|" +name+"|"+ players[0].getNickname() + "|" +
                            players[1].getNickname() + "|" + players[2].getNickname() + "|" +
                            players[3].getNickname());
                    player.write("GAME|"+ id + "|" + name +"|TURN|"+playerTurn.getNickname());
                } catch (Exception e) {
                    e.printStackTrace();
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
                synchronized (players) {
                    for (User player : players)
                        try {
                            player.write("GAME|" + id + "|" + name + "|ROLL|" + playerTurn.getNickname() + "|" + roll);
                        } catch (Exception e) {
                            usersClosedSocets.add(player);
                            e.printStackTrace();
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
                                e.printStackTrace();
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
                            e.printStackTrace();
                        }
                }
                moveBack = false;

                if (playerSquare[4] != 6)
                    passTurn();
                else {
                    synchronized (players) {
                        for (User player : players)
                            try {
                                player.write("GAME|" + id + "|" + name + "|TURN|" + playerTurn.getNickname());
                            } catch (Exception e) {
                                usersClosedSocets.add(player);
                                e.printStackTrace();
                            }
                    }
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
    private void passTurn() {
        if (turnInt < 3)
            turnInt++;
        else
            turnInt = 0;
        playerTurn=players[turnInt];

        synchronized (players) {
            for (User player : players)
                try {
                    player.write("GAME|"+ id + "|" + name +"|TURN|"+playerTurn.getNickname());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
