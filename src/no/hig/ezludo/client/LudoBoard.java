package no.hig.ezludo.client;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Kristian
 * date 01.11.2015.
 * This class contains a scalable ludoboard insida a JFrame. This is used to define the X and Y coordinates
 * for every square at the ludoboard. Output from X and Y are used further to define LudoBoardCoordinates.java.
 *
 */
public class LudoBoard extends JPanel {
    private transient Image board;
    public int boardWidth;
    public int boardHeight;

    /**
     * Constructor to set the ludoboard to JFrame
     * */
    public LudoBoard() {
        ImageIcon tmpBoard = new ImageIcon (getClass().getResource("/res/board.png"));
        board = tmpBoard.getImage();
        repaint();
    }

    /**
     * Repaint the ludoboard when JFrame changes size
     * */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(board, 0, 0, getWidth(), getHeight(), this);
        boardHeight=getHeight();
        boardWidth=getWidth();

    }
    /**
     * MAIN
     * Mouselistener is used to find coordinates on loaded image.
     * X is divide by the width
     * Y is divided by the height
     *
     * The outpur is:
     * X Coord
     * Y Coord
     * --------------------
     * */
    public static void main(String[] args) {
        JFrame jFrame= new JFrame();
        LudoBoard ludoBoard;
        ludoBoard = new LudoBoard();
        ludoBoard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double x = e.getX();
                double y = e.getY();
                double boardWidth = ludoBoard.boardWidth;
                double boardHeight = ludoBoard.boardHeight;
                double coordX = x / boardWidth;
                double coordY = y / boardHeight;
                System.out.print(coordX);
                System.out.print("\n");
                System.out.print(coordY);
                System.out.print("\n");
                System.out.print("-------------------------------");
                System.out.print("\n");
            }


        });
        jFrame.add(ludoBoard);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
