package no.hig.ezludo.client;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Kristian on 01.11.2015.
 */
public class LudoBoard extends JPanel {
    private Dimension size;
    private Image board;
    public int boardWidth;
    public int boardHeight;

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
        g2d.drawImage(board, 0, 0, getWidth() , getHeight() , this);
        boardHeight=getHeight();
        boardWidth=getWidth();
    }

    //// MAIN FJERNES
    public static void main(String[] args) {
        JFrame jFrame= new JFrame();
        LudoBoard ludoBoard;
        ludoBoard = new LudoBoard();
        ludoBoard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double x = e.getX();
               // System.out.print("X: ");
               // System.out.print(x);
               // System.out.print(" - ");
                double y = e.getY();
               // System.out.print("Y: ");
               // System.out.print(y);
               // System.out.print(" || ");
                double boardWidth = ludoBoard.boardWidth;
                double boardHeight = ludoBoard.boardHeight;
                double coordX = x / boardWidth;
                double coordY = y / boardHeight;
                System.out.print(coordX);
                System.out.print("\n");
                System.out.print(coordY);
                System.out.print("\n");
                double backX = coordX * boardWidth;
                double backY = coordY * boardHeight;
                //System.out.print(backX);
               // System.out.print(" - ");
                //System.out.print(backY);
               // System.out.print("\n");



            }
        });
        jFrame.add(ludoBoard);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);


    }

}
