/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import common.MyCharacter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Daniel
 */
public class ListenerThread implements Runnable {

    protected Socket clientSocket;
    protected int clientID;
    MyCharacter input;

    public ListenerThread(Socket clientSocket, int clientID) {
        this.clientSocket = clientSocket;
        this.clientID = clientID;
    }

    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            
            while (true) {
                Thread.sleep(2000);
            }
        } catch (IOException e) {

        } catch (InterruptedException e) {

        }
    }

}
