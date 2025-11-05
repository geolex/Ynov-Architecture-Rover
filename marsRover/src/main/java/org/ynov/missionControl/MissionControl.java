// java
package org.ynov.missionControl;

import org.ynov.communication.*;
import org.ynov.world.Planet;
import org.ynov.world.TypeElement;
import org.ynov.world.Vector2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Vector;
import java.io.*;



public class MissionControl implements KeyListener {
    private static final java.io.BufferedReader CONSOLE_READER = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

    Map map;
    static boolean isPromptingUser = false;
    Vector<Instruction> currentInstructions = new Vector<Instruction>();
    Connection connection;
    private Vector2 lastRoverPos = null;

    public MissionControl(ICommunicator communicator) {
        System.out.println("Welcome to Kerbal's Mars Rover Program");
        this.connection = communicator.HostCommunication();
        map = new Map(Planet.getPlanet().getWidth(),  Planet.getPlanet().getHeight());

        // Lecture du handshake initial envoyé par le rover (position + orientation)
        try {
            String init = connection.in.readLine();
            if (init != null && !init.isBlank()) {
                Information info = Information.Decode(init);
                // afficher la position et l'orientation reçues
                System.out.println("Position initiale du rover : (" + info.position.x + "," + info.position.y + ")");
                System.out.println("Orientation initiale : " + info.orientation);
                UpdateMap(info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        PromptUser();
    }
/*
    private void PromptUser(){
        System.out.println("What are your instructions?");
        isPromptingUser = true;
        ReadString();
    }*/

    private void PromptUser(){
        System.out.println("What are your instructions?");
        System.out.print("> ");
        isPromptingUser = true;

        String line;
        try {
            line = CONSOLE_READER.readLine();
        } catch (IOException e) {
            System.err.println("Erreur lecture console: " + e.getMessage());
            isPromptingUser = false;
            return;
        }

        if (line == null) {
            isPromptingUser = false;
            return;
        }

        // Autoriser seulement z q s d (maj/min) et espaces
        if (!line.matches("^[zqsdZQSD\\s]*$")) {
            System.out.println("Commande refusée : seuls les caractères z, q, s, d sont autorisés.");
            isPromptingUser = false;
            return;
        }
        String filtered = line.replaceAll("\\s+", "").toLowerCase();

        // Construire la liste d'instructions attendue par le rover
        currentInstructions.clear();
        for (char c : filtered.toCharArray()) {
            switch (c) {
                case 'z' -> currentInstructions.add(new Instruction(InstructionEnum.Forward));
                case 's' -> currentInstructions.add(new Instruction(InstructionEnum.Backward));
                case 'q' -> currentInstructions.add(new Instruction(InstructionEnum.TurnLeft));
                case 'd' -> currentInstructions.add(new Instruction(InstructionEnum.TurnRight));
                default -> {} // impossible grâce au filtre
            }
        }
        if (connection != null && connection.out != null) {
            try {
                connection.out.println(Instruction.Encode(currentInstructions));
                connection.out.flush();
                System.out.println("Envoyé: " + Instruction.Encode(currentInstructions));
                isPromptingUser = false;
                ListenForReply();
            } catch (Exception e) {
                System.err.println("Erreur envoi: " + e.getMessage());
                isPromptingUser = false;
            }
        } else {
            System.err.println("Connexion indisponible pour envoyer les instructions.");
            isPromptingUser = false;
        }
    }

    private void ReadString(){
        currentInstructions.clear();

        int input;
        while (true) {
            try {
                if (!((input = System.in.read()) != '\n')) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            currentInstructions.add(new Instruction(switch (input) {
                case 'z' -> InstructionEnum.Forward;
                case 's' -> InstructionEnum.Backward;
                case 'q' -> InstructionEnum.TurnLeft;
                case 'd' -> InstructionEnum.TurnRight;
                default -> null;
            }));
        }
        connection.out.println(Instruction.Encode(currentInstructions));
        isPromptingUser = false;
        ListenForReply();
    }

    private void ListenForReply(){
        try {
            String data;
            while ((data = connection.in.readLine()) != null) {
                System.out.println(data);
                Information info = Information.Decode(data);
                UpdateMap(info);
            }
        } catch (java.net.SocketException se) {
            System.err.println("Connexion perdue (socket reset) : " + se.getMessage());
            // Ne rappeler pas PromptUser() si la connexion est morte
            isPromptingUser = false;
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void UpdateMap(Information info) {
        // effacer ancienne position du rover

        if (lastRoverPos != null) {
            map.setCell(lastRoverPos, TypeElement.EMPTY);
        }
        map.setCell(info.position, TypeElement.ROVER);
        lastRoverPos = info.position;

        if (info.success) {



        } else {
            Vector2 direction = switch (info.orientation){
                case North -> Vector2.UP;
                case East -> Vector2.RIGHT;
                case South -> Vector2.DOWN;
                case West -> Vector2.LEFT;
                default -> Vector2.ZERO;
            };

            map.setCell(Vector2.add(info.position, direction).modulo(map.getWidth(), map.getHeight()), TypeElement.OBSTACLE);
        }

        // afficher infos en console
        System.out.println("Rover -> position: (" + info.position.x + "," + info.position.y + "), orientation: " + info.orientation + ", success: " + info.success);

        map.printMapAscii();
        PromptUser();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!isPromptingUser) return;
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            connection.out.println(Instruction.Encode(currentInstructions));
            System.out.println(connection.out.toString());
            isPromptingUser = false;
        }

        InstructionEnum instEnum = switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> InstructionEnum.Forward;
            case KeyEvent.VK_DOWN -> InstructionEnum.Backward;
            case KeyEvent.VK_LEFT -> InstructionEnum.TurnLeft;
            case KeyEvent.VK_RIGHT -> InstructionEnum.TurnRight;
            default -> null;
        };

        if(instEnum != null) currentInstructions.add(new Instruction(instEnum));
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}