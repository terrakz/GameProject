package server;

import common.MyCharacter;
import java.awt.Image;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.*;
import javax.swing.ImageIcon;

public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 4444;
        ServerSocket serverSocket = new ServerSocket(port);
        boolean paused = false;
        int numberOfClients = 0;

        // Load images
        Image characterCurrent = new ImageIcon(ServerTest.class.getResource("char_down.gif")).getImage();
        Image characterUp = new ImageIcon(ServerTest.class.getResource("char_up.gif")).getImage();
        Image characterDown = new ImageIcon(ServerTest.class.getResource("char_down.gif")).getImage();
        Image characterLeft = new ImageIcon(ServerTest.class.getResource("char_left.gif")).getImage();
        Image characterRight = new ImageIcon(ServerTest.class.getResource("char_right.gif")).getImage();
        Image chest = new ImageIcon(ServerTest.class.getResource("chest.gif")).getImage();

        System.out.println("Starting...");

        while(!paused){
            Socket clientSocket = null;
            
            try {
                clientSocket = serverSocket.accept();
                
            } catch(IOException e){
                
            }
            new Thread(new PlayerThread(clientSocket, numberOfClients++)).start();
            //new Thread(new ListenerThread(clientSocket, numberOfClients++)).start();
            System.out.println("NEW THREAD");
        }
    }

}
