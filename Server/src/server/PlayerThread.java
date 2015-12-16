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
public class PlayerThread implements Runnable {

    protected Socket clientSocket;
    protected int numberOfClients;
    MyCharacter input;

    public PlayerThread(Socket clientSocket, int numberOfClients) {
        this.clientSocket = clientSocket;
        this.numberOfClients = numberOfClients;
    }

    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

            System.out.println("PlayerThread started.");
            MyCharacter char1 = new MyCharacter(50,50,"Player ",numberOfClients);
            out.writeObject(char1);
            System.out.println("Sent object.");
            
            while (true) {
                Thread.sleep(2000);
                System.out.println("Waiting for input from client...");
                input = (MyCharacter) in.readObject();
                System.out.println("Character name: " + input.getName());
                System.out.println("Current x value: " + input.getX());
                System.out.println("Current y value: " + input.getY());
                System.out.println("ID is: " + input.getID());
            }
        } catch (IOException e) {

        } catch (InterruptedException e) {

        } catch (ClassNotFoundException e) {

        }
    }

}
