package no.hig.ezludo.client;

import javafx.fxml.FXML;
import javafx.scene.image.Image;

import javax.swing.text.html.ImageView;
import java.io.File;
import java.util.Random;

/**
 * Created by Kristian on 12.11.2015.
 */
public class GameController {
    @FXML private ImageView dice;
    private int diceNr;
    private final int diceMin = 1;
    private final int diceMax = 6;
    private final int rounds = 5;

    public GameController() { }


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
