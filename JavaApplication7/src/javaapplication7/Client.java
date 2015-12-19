package javaapplication7;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import java.util.Scanner;
import java.io.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Client extends Applet implements Runnable, KeyListener {

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

    /*public static void main(String[] args){
        Client client = new Client();
        client.init();
    }*/
    
    public void init() {
        setSize(100, 100);
        addKeyListener(this);
        try {
            System.out.println("Connecting...");
            socket = new Socket("localhost", 4444);
            System.out.println("connection succesful.");
            in = new DataInputStream(socket.getInputStream());
            playerid = in.readInt();
            out = new DataOutputStream(socket.getOutputStream());
            Input input = new Input(in, this);
            Thread thread = new Thread(input);
            thread.start();
            Thread thread2 = new Thread(this);
            thread2.start();
        } catch (Exception e) {
            System.out.println("unable to start cluient");
        }
    }

    public void updateCoordinates(int pid, int x2, int y2, int image) throws ArrayIndexOutOfBoundsException {
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
    }

    public void paint(Graphics g) {
        for (int i = 0; i < 10; i++) {
            //g.drawOval(x[i], y[i], 5, 5);
            g.drawImage((Image) currentImage[i], x[i], y[i], this);
        }
    }

    public boolean noCollision(String dir) {
        boolean result = true;

        switch (dir) {
            case "right": {
                for (int i = 0; i < 10; i++) {
                    //if(i == playerid){i++;}
                    if (playerx + 32 == x[i] && playery == y[i]) {
                        result = false;
                    }
                }
                break;
            }
            case "left": {
                for (int i = 0; i < 10; i++) {
                    //if(i == playerid){i++;}
                    if (playerx - 32 == x[i] && playery == y[i]) {
                        result = false;
                    }
                }
                break;
            }
            case "up": {
                for (int i = 0; i < 10; i++) {
                    //if(i == playerid){i++;}
                    if (playerx == x[i] && playery - 32 == y[i]) {
                        result = false;
                    }
                }
                break;
            }
            case "down": {
                for (int i = 0; i < 10; i++) {
                    //if(i == playerid){i++;}
                    if (playerx == x[i] && playery + 32 == y[i]) {
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
                playerx += 32;
            }
            if (left == true && noCollision("left") == true) {
                playerx -= 32;
            }
            if (down == true && noCollision("down") == true) {
                playery += 32;
            }
            if (up == true && noCollision("up") == true) {
                playery -= 32;
            }
            if (right || left || up || down) {
                try {
                    out.writeInt(playerid);
                    out.writeInt(playerx);
                    out.writeInt(playery);
                    out.writeInt(playerimage);
                } catch (Exception e) {
                    System.out.println("error sending vcoordineates.");
                }
            }

            repaint();
            try {
                Thread.sleep(300);
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
                    e.printStackTrace();
                }
            }
        }
    }
}
