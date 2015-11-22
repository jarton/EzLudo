package no.hig.ezludo.server;

import no.hig.ezludo.server.commands.Command;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

/**
 * Created by jdr on 29/10/15.
 */
public class Game {
    private User players[] = new User[4];
    private int[] player0 = new int[5];
    private int[] player1 = new int[5];
    private int[] player2 = new int[5];
    private int[] player3 = new int[5];
    private HashMap<String, int[]> userPlaces = new HashMap<>();
    private String name = "Random Game";
    private User playerTurn;
    private int id = -1;
    private final int diceMin = 1;
    private final int diceMax = 6;
    private final int victorySquare = 59;
    private int turnInt;

    public Game(User players[]) {
        this.players = players;
        turnInt = 0;
        playerTurn = players[turnInt];
        initPlaces();
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }



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

    public void gameHandler(Command cmd, Vector<User> usersClosedSocets) {
        if (cmd.getRawCmd().startsWith("GAME|" + id + "|" + name + "|ROLL")) {
            String roll;
            if (playerTurn == cmd.getUser()) {
                roll = rollDices();
                synchronized (players) {
                    for (User player : players)
                        try {
                            player.write("GAME|" + id + "|" + name + "|ROLL|" + playerTurn.getNickname() + "|" + roll);
                            userPlaces.get(playerTurn.getNickname())[4] = Integer.parseInt(roll);
                        } catch (Exception e) {
                            usersClosedSocets.add(player);
                            e.printStackTrace();
                        }
                }
            }
        }
        if (cmd.getRawCmd().startsWith("GAME|" + id + "|" + name + "|MOVE")) {
            if (playerTurn == cmd.getUser()) {
                int playerSquare[] = userPlaces.get(playerTurn.getNickname());
                int pieceToMove = Integer.parseInt(cmd.getRawCmd().split("\\|")[4]);

                if ((playerSquare[pieceToMove] == 0) && (playerSquare[4] == 6))
                    playerSquare[pieceToMove] += playerSquare[4];
                else if (playerSquare[pieceToMove] != 0 && playerSquare[4] != 6)
                    playerSquare[pieceToMove] += playerSquare[4];

                if (playerSquare.equals(victorySquare)) {
                    //TODO PLAYER WON ALERT EVERYBODY
                }
                synchronized (players) {
                    for (User player : players)
                        try {
                            player.write("GAME|" + id + "|" + name + "|MOVE|" + playerTurn.getNickname() + "|" +
                                    String.valueOf(pieceToMove) + "|" +
                                    userPlaces.get(playerTurn.getNickname())[pieceToMove]);
                        } catch (Exception e) {
                            usersClosedSocets.add(player);
                            e.printStackTrace();
                        }
                }
                if (playerSquare[4] != 6)
                    passTurn();
                else {
                    synchronized (players) {
                        for (User player : players)
                            try {
                                player.write("GAME|" + id + "|" + name + "|TURN|" + playerTurn.getNickname());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                }
            }
        }
    }

    public String rollDices() {
            int diceNr = randomInt(diceMin, diceMax);
        return String.valueOf(diceNr);
    }


    private int randomInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

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
