package org.ynov.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CommunicatorTCP implements ICommunicator {
    private int port = 9090;

    public Connection HostCommunication() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is running and waiting for client connection...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            return new Connection(in, out);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Connection ConnectToCommunication() {
        try {
            System.out.println("Connecting to server");

            Socket clientSocket = new Socket("localhost", port);
            System.out.println("Connected to server");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            return new Connection(in, out);
        }catch (IOException e){

            return null;
        }
    }

    @Override
    public void CloseCommunication(Connection connection) {

    }
}
