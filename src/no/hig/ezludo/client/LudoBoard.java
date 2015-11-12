package no.hig.ezludo.client;


import javax.swing.*;
import java.awt.*;

/**
 * Created by Kristian on 01.11.2015.
 */
public class LudoBoard extends JPanel {
    private Dimension size;
    private Image board;

    public LudoBoard() {
        ImageIcon tmpBoard = new ImageIcon (getClass().getResource("/res/board.png"));
        int height = tmpBoard.getIconHeight();
        int width = tmpBoard.getIconWidth();
        size = new Dimension(width, height);
        board = tmpBoard.getImage();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(board, 0, 0,900,600, this);
    }


    //// MAIN FJERNES
    public static void main(String[] args) {
        JFrame jFrame= new JFrame();
        LudoBoard ludoBoard;
        ludoBoard = new LudoBoard();
        jFrame.add(ludoBoard);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);


    }

}
