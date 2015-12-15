/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class ServerTest {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 4444;
        System.out.println("Starting...");
        
        try (
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
            System.out.println("Server running on port " + serverSocket.getLocalPort());
            Thread.sleep(1000);
            System.out.println("Client connected on port " + clientSocket.getPort());
            Thread.sleep(1000);
            String input;
            String output;
            int iInput;

            MyProtocol mp = new MyProtocol();
            while (true) {
                System.out.println("Waiting for input from client...");
                //input = in.readLine();
                input = in.readLine();
                iInput = Integer.parseInt(input);
                System.out.println("Input: " + input);
                //output = mp.processInput(Integer.parseInt(input));
                output = mp.processInput(iInput);
                out.println(output);
                //Thread.sleep(3000);
            }

        } catch (IOException e) {

        }
    }

}
