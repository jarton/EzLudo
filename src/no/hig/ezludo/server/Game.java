package no.hig.ezludo.server;

import java.util.Random;
import java.util.Vector;

/**
 * Created by jdr on 29/10/15.
 */
public class Game {
    private User players[] = new User[4];
    private String name = "Random Game";
    private int id = -1;
    private int diceNr;
    private final int diceMin = 1;
    private final int diceMax = 6;
    private final int rounds = 5;

    public Game(User players[]) {
        this.players = players;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int rollDices() {
        for (int i =0; i<=rounds; i++) {
            diceNr = randomInt(diceMin, diceMax);
            // Show Image .. set image in imgview dice+diceNr+.png
            delay(1000);
        }
        return diceNr;
    }

    public void delay(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {}
    }

    public static int randomInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
