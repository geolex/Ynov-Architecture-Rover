package org.ynov.rover;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ynov.shared.Instruction;
import org.ynov.shared.OrientationEnum;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(12345);
        ObjectMapper mapper = new ObjectMapper();

        // Création de la planète
        Planet planet = new Planet("Mars", 10, 5);

        // Création du rover sur la planète
        Rover rover = new Rover(OrientationEnum.North);

        System.out.println("Serveur en écoute...");
        Socket clientSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String line;
        while ((line = in.readLine()) != null) {
            Instruction instruction = mapper.readValue(line, Instruction.class);
            rover.move(instruction.instruction);

            System.out.println("Nouvelle position : " + rover.getPosition().x + ", " + rover.getPosition().y);
            System.out.println("Orientation : " + rover.getOrientation());
        }

        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}
