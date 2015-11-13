package no.hig.ezludo.server.commands;
import no.hig.ezludo.server.Game;

import java.util.Random;

/**
 * Created by Kristian on 13.11.2015.
 */
public class RollDice  {
    private int diceNr;
    private final int diceMin = 1;
    private final int diceMax = 6;
    private final int rounds = 5;

  //  public RollDice() {}

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
