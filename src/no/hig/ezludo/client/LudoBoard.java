package no.hig.ezludo.client;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Kristian
 * date 01.11.2015.
 * This class inclues a scalable ludoboard insida a JFrame. This is used to define the X and Y coordinates
 * for every square. Output from X and Y are used further to define LudoBoardCoordinates.java.
 *
 */
public class LudoBoard extends JPanel {
    private Dimension size;
    private Image board;

    private Dimension redSize;
    private Image red;
    public int boardWidth;
    public int boardHeight;

    public LudoBoard() {
        // get Ludo board Image
        ImageIcon tmpBoard = new ImageIcon (getClass().getResource("/res/board.png"));
        int height = tmpBoard.getIconHeight();
        int width = tmpBoard.getIconWidth();
        size = new Dimension(width, height);
        board = tmpBoard.getImage();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        // Define g2d
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // add Ludo board
        g2d.drawImage(board, 0, 0, getWidth(), getHeight() , this);

        // Get lodo board height and width for int and double
        boardHeight=getHeight();
        boardWidth=getWidth();

    }
    public static void main(String[] args) {
        JFrame jFrame= new JFrame();
        LudoBoard ludoBoard;
        ludoBoard = new LudoBoard();

        // Mouselistener is used to find coordinates on loaded image
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
