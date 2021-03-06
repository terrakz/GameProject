package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import common.MyCharacter;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.KeyStroke;

public class Client extends JFrame {

    private int x, y;
    private int numberOfClients;
    
    Image characterCurrent = new ImageIcon(Client.class.getResource("char_down.gif")).getImage();
    Image characterUp = new ImageIcon(Client.class.getResource("char_up.gif")).getImage();
    Image characterDown = new ImageIcon(Client.class.getResource("char_down.gif")).getImage();
    Image characterLeft = new ImageIcon(Client.class.getResource("char_left.gif")).getImage();
    Image characterRight = new ImageIcon(Client.class.getResource("char_right.gif")).getImage();
    Image chest = new ImageIcon(Client.class.getResource("chest.gif")).getImage();

    Window window = new Window();
    static MyCharacter dummy = null;

    // Constructor
    public Client() {
        // Handle character moving up
        Action upAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                characterCurrent = characterUp;
                dummy.Up();
                window.repaint();
            }
        };
        // Handle character moving down
        Action downAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                characterCurrent = characterDown;
                dummy.Down();
                window.repaint();
            }
        };
        // Handle character moving left
        Action leftAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                characterCurrent = characterLeft;
                dummy.Left();
                window.repaint();
            }
        };
        // Handle character moving right
        Action rightAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                characterCurrent = characterRight;
                dummy.Right();
                window.repaint();
            }
        };

        // Input and action mapping
        InputMap inputMap = window.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = window.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("UP"), "upAction");
        actionMap.put("upAction", upAction);
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "downAction");
        actionMap.put("downAction", downAction);
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "leftAction");
        actionMap.put("leftAction", leftAction);
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "rightAction");
        actionMap.put("rightAction", rightAction);

        add(window);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String args[]) throws IOException {

        String host = "192.168.1.248";
        int port = 4444;

        try (
                Socket clientSocket = new Socket(host, port);
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ) {
            
            System.out.println("Retrieving object...");
            Thread.sleep(1000);
            dummy = (MyCharacter) in.readObject();
            Client client = new Client();
            client.setTitle("The Best Game in the World!");
            client.setVisible(true);

            while (true) {
                out.reset();
                
                Thread.sleep(2000);
                System.out.println("Character name: " + dummy.getName());
                System.out.println("Character x: " + dummy.getX());
                System.out.println("Character y: " + dummy.getY());
                out.writeObject(dummy);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class Window extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.setBackground(Color.WHITE);
            g.drawImage((Image) chest, 125, 70, rootPane);
            // Draw other clients characters

            g.drawImage((Image) characterCurrent, dummy.getX(), dummy.getY(), rootPane);
            for(int i = 0; i <= numberOfClients; i++){
                
            }
        }
    }
}
