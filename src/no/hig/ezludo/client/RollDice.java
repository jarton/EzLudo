package no.hig.ezludo.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * Created by Kristian on 13.11.2015.
 */
public class RollDice extends JPanel  {
    private int diceNr;
    private final int diceMin = 1;
    private final int diceMax = 6;
    private final int rounds = 8;
    private Dimension size;
    private Image dice;

    public RollDice() { }

    public void showImage(int nr) {
        ImageIcon tmpDice = new ImageIcon (getClass().getResource("/res/dices/dice"+nr+".png"));
        int diceHeight = tmpDice.getIconHeight();
        int diceWidth = tmpDice.getIconWidth();
        size = new Dimension(diceWidth, diceHeight);
        dice= tmpDice.getImage();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(dice, 0, 0, getWidth(), getHeight(), this);
    }

    public void rollDices()  {
        Thread diceLoadingThread = new Thread(new Runnable() {
            public void run() {
                for (int i =0; i<=rounds; i++) {
                    diceNr = randomInt(diceMin, diceMax);
                    showImage(diceNr);
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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


    //// MAIN FJERNES////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        JFrame jFrame= new JFrame();
        RollDice rollDice;
        rollDice = new RollDice();

        // Mouselistener is used to find coordinates on loaded image
        rollDice.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rollDice.rollDices();
            }
        });

        jFrame.add(rollDice);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        }
    }