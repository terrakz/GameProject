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

    boolean left, down, right, up;

    int playerx;
    int playery;

    public void init() {
        setSize(100, 100);
        addKeyListener(this);
        try {
            System.out.println("Connecting...");
            socket = new Socket("82.100.67.62", 4444);
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

    public void updateCoordinates(int pid, int x2, int y2) {
        this.x[pid] = x2;
        this.y[pid] = y2;
    }

    public void paint(Graphics g) {
        for (int i = 0; i < 10; i++) {
            //g.drawOval(x[i], y[i], 5, 5);
            g.drawImage((Image) characterCurrent, x[i], y[i], this);
        }
    }

    public boolean noCollision(String dir) {
        boolean result = true;
        
        switch (dir) {
            case "right": {
                for (int i = 0; i < 10; i++) {
                    if(playerx+32 == x[i] && playery == y[i]){
                        result = false;
                    }
                }
            }
            case "left": {
                for (int i = 0; i < 10; i++) {
                    if(playerx-32 == x[i] && playery == y[i]){
                        result = false;
                    }
                }
            }
            case "up": {
                for (int i = 0; i < 10; i++) {
                    if(playerx == x[i] && playery-32 == y[i]){
                        result = false;
                    }
                }
            }
            case "down": {
                for (int i = 0; i < 10; i++) {
                    if(playerx == x[i] && playery-32 == y[i]){
                        result = false;
                    }
                }
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
                } catch (Exception e) {
                    System.out.println("error sending vcoordineates.");
                }
            }

            repaint();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 37) {
            left = true;
        }
        if (e.getKeyCode() == 38) {
            up = true;
        }
        if (e.getKeyCode() == 39) {
            right = true;
        }
        if (e.getKeyCode() == 40) {
            down = true;
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
                    client.updateCoordinates(playerid, x, y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
