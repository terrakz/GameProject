package javaapplication7;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import java.io.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Client extends JFrame implements Runnable, KeyListener {

    Image characterCurrent = new ImageIcon(Client.class.getResource("char_down.gif")).getImage();
    Image characterUp = new ImageIcon(Client.class.getResource("char_up.gif")).getImage();
    Image characterDown = new ImageIcon(Client.class.getResource("char_down.gif")).getImage();
    Image characterLeft = new ImageIcon(Client.class.getResource("char_left.gif")).getImage();
    Image characterRight = new ImageIcon(Client.class.getResource("char_right.gif")).getImage();
    Image chest = new ImageIcon(Client.class.getResource("chest.gif")).getImage();

    static Socket socket;
    static DataOutputStream out;
    static DataInputStream in;

    int playerid;

    int[] x = new int[10];
    int[] y = new int[10];
    Image[] currentImage = new Image[10];

    boolean left, down, right, up;

    int playerx;
    int playery;
    int playerimage = 0;
    int playerspeed = 8;
    int playersize = characterUp.getHeight(this);

    Graphics graphicBuffer;
    Image offScreenRender;
    Dimension dim;

    static Client client = new Client();

    public Client() {
        setTitle("The worst game in the world");
        setBackground(Color.WHITE);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        client.init();
    }

    public void init() {
        dim = getSize();
        offScreenRender = createImage(dim.width, dim.height);
        graphicBuffer = offScreenRender.getGraphics();
        addKeyListener(this);
        try {
            System.out.println("Connecting...");
            socket = new Socket("localhost", 4444);
            System.out.println("Connection successful");
            in = new DataInputStream(socket.getInputStream());
            playerid = in.readInt();
            out = new DataOutputStream(socket.getOutputStream());
            Input input = new Input(in, this);
            Thread thread = new Thread(input);
            thread.start();
            Thread thread2 = new Thread(this);
            thread2.start();
        } catch (Exception e) {
            System.out.println("Unable to start client");
        }
    }

    public void updateCoordinates(int pid, int x2, int y2, int image) throws ArrayIndexOutOfBoundsException {
        try {
            this.x[pid] = x2;
            this.y[pid] = y2;

            switch (image) {
                case 0: {
                    currentImage[pid] = characterUp;
                    break;
                }
                case 1: {
                    currentImage[pid] = characterDown;
                    break;
                }
                case 2: {
                    currentImage[pid] = characterLeft;
                    break;
                }
                case 3: {
                    currentImage[pid] = characterRight;
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("DEBUG: Array index was out of bounds. Index: " + pid);
        }
    }

    public void paint(Graphics g) {
        graphicBuffer.clearRect(0, 0, dim.width, dim.height);
        for (int i = 0; i < 10; i++) {
            graphicBuffer.drawImage((Image) currentImage[i], x[i], y[i], this);
            graphicBuffer.drawString("Player " + i, x[i] - 3, y[i] - 6);
        }
        g.drawImage(offScreenRender, 0, 0, this);
    }

    public void update(Graphics g) {
        paint(g);
    }

    public boolean noCollision(String dir) {
        boolean result = true;

        switch (dir) {
            case "right": {
                for (int i = 0; i < 10; i++) {
                    if (playerx + playersize == x[i] && (playery == y[i] || (playery + playersize > y[i] && playery - playersize < y[i]))) {
                        result = false;
                    }
                }
                break;
            }
            case "left": {
                for (int i = 0; i < 10; i++) {
                    if (playerx - playersize == x[i] && (playery == y[i] || (playery + playersize > y[i] && playery - playersize < y[i]))) {
                        result = false;
                    }
                }
                break;
            }
            case "up": {
                for (int i = 0; i < 10; i++) {
                    if (playery - playersize == y[i] && (playerx == x[i] || (playerx + playersize > x[i] && playerx - playersize < x[i]))) {
                        result = false;
                    }
                }
                break;
            }
            case "down": {
                for (int i = 0; i < 10; i++) {
                    if (playery + playersize == y[i] && (playerx == x[i] || (playerx + playersize > x[i] && playerx - playersize < x[i]))) {
                        result = false;
                    }
                }
                break;
            }
        }
        return result;
    }

    public void run() {
        while (true) {
            if (right == true && noCollision("right") == true) {
                playerx += playerspeed;
            }
            if (left == true && noCollision("left") == true) {
                playerx -= playerspeed;
            }
            if (down == true && noCollision("down") == true) {
                playery += playerspeed;
            }
            if (up == true && noCollision("up") == true) {
                playery -= playerspeed;
            }
            if (right || left || up || down) {
                try {
                    out.writeInt(playerid);
                    out.writeInt(playerx);
                    out.writeInt(playery);
                    out.writeInt(playerimage);
                } catch (Exception e) {
                    System.out.println("Couldn't send coordinates");
                }
            }
            client.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 37) {
            left = true;
            playerimage = 2;
        }
        if (e.getKeyCode() == 38) {
            up = true;
            playerimage = 0;
        }
        if (e.getKeyCode() == 39) {
            right = true;
            playerimage = 3;
        }
        if (e.getKeyCode() == 40) {
            down = true;
            playerimage = 1;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 37) {
            left = false;
        }
        if (e.getKeyCode() == 38) {
            up = false;
        }
        if (e.getKeyCode() == 39) {
            right = false;
        }
        if (e.getKeyCode() == 40) {
            down = false;
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    class Input implements Runnable {

        DataInputStream in;
        Client client;

        public Input(DataInputStream in, Client c) {
            this.in = in;
            this.client = c;
        }

        public void run() {
            while (true) {
                try {
                    int playerid = in.readInt();
                    int x = in.readInt();
                    int y = in.readInt();
                    int image = in.readInt();
                    client.updateCoordinates(playerid, x, y, image);
                } catch (IOException e) {
                    System.out.println("Server died.");
                    // Handle a move to the secondary server here
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
