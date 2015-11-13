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

        // get red Chip image
        ImageIcon tmpRed = new ImageIcon (getClass().getResource("/res/dice1.png"));
        int redHeight = tmpRed.getIconHeight();
        int redWidth = tmpRed.getIconWidth();
        redSize = new Dimension(redWidth, redHeight);
        red = tmpRed.getImage();

        repaint();
    }

    @Override
    public void paint(Graphics g) {
        // Define g2d
        super.paint(g);
        double redStart[][] = new double[4][4];
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // add Ludo board
        g2d.drawImage(board, 0, 0, getWidth(), getHeight() , this);

        // Get lodo board height and width for int and double
        boardHeight=getHeight();
        boardWidth=getWidth();
        double doubleBoardHeight=getHeight();
        double doubleBoardWidth=getWidth();

        // Define red first position
        redStart[1][1]=0.7671381936887922;
        redStart[1][2]=0.10062893081761007;

        // calculate new position from cordinates and size og ludo board
        double redCordX = redStart[1][1] * doubleBoardWidth;
        double redCordY = redStart[1][2] * doubleBoardHeight;

        // convert from int to double
        int intX = (int) redCordX;
        int intY = (int) redCordY;

        // draw image
        // g2d.drawImage(image, Xcord, YCord, Img Widgh , IMG Height , this);
        g2d.drawImage(red, (intX), (intY), boardWidth/20, boardHeight/20, this);


    }

    //// MAIN FJERNES////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        JFrame jFrame= new JFrame();
        LudoBoard ludoBoard;
        ludoBoard = new LudoBoard();

        // Mouselistener is used to find coordinates on loaded image
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
