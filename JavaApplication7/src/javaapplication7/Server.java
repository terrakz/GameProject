package javaapplication7;

import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static ServerSocket serverSocket;
    static Users[] user = new Users[10];

    public static void main(String[] args) throws Exception {
        System.out.println("Starting server...");
        serverSocket = new ServerSocket(4444);
        System.out.println("Server started...");
        while (true) {
            Socket socket = serverSocket.accept();
            for (int i = 0; i < 10; i++) {
                if (user[i] == null) {
                    System.out.println("Connection from: " + socket.getInetAddress());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    DataInputStream in = new DataInputStream(socket.getInputStream());

                    user[i] = new Users(out, in, user, i);
                    Thread th = new Thread(user[i]);
                    th.start();
                    break;
                }
            }
        }
    }
}

    class Users implements Runnable {

        DataOutputStream out;
        DataInputStream in;
        Users[] user = new Users[10];
        String name;
        int playerid;
        int playeridin;
        int xin;
        int yin;
        int imagein;

        public Users(DataOutputStream out, DataInputStream in, Users[] user, int pid) {
            this.out = out;
            this.in = in;
            this.user = user;
            this.playerid = pid;
        }

        public void run() {
            try {
                out.writeInt(playerid);
            } catch (IOException e) {
                System.out.println("Failed to send PlayerID");
            }
            while (true) {
                try {
                    playeridin = in.readInt();
                    xin = in.readInt();
                    yin = in.readInt();
                    imagein = in.readInt();
                    for (int i = 0; i < 10; i++) {
                        if (user[i] != null) {
                            user[i].out.writeInt(playeridin);
                            user[i].out.writeInt(xin);
                            user[i].out.writeInt(yin);
                            user[i].out.writeInt(imagein);
                        }
                    }
                } catch (IOException e) {
                    user[playerid] = null;
                    System.out.println("User " + playerid + " has left.");
                    break;
                }
            }
        }

    }