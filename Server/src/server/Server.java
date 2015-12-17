package server;

import common.MyCharacter;
import java.awt.Image;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.*;
import javax.swing.ImageIcon;

public class Server {

    static int NUMBER_OF_CLIENTS = 0;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 4444;
        ServerSocket serverSocket = new ServerSocket(port);
        boolean paused = false;

        System.out.println("Starting...");

        while(!paused){
            Socket clientSocket = null;
            
            try {
                clientSocket = serverSocket.accept();
                
            } catch(IOException e){
                
            }
            new Thread(new PlayerThread(clientSocket, NUMBER_OF_CLIENTS)).start();
            //new Thread(new ListenerThread(clientSocket, NUMBER_OF_CLIENTS++)).start();
            System.out.println("NEW THREAD with index: " + NUMBER_OF_CLIENTS);
            NUMBER_OF_CLIENTS++;
        }
    }

}
