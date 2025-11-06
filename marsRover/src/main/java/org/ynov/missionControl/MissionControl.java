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
        map = new Map(Planet.getPlanet().getSize().x,  Planet.getPlanet().getSize().y);

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
    private Vector2 toMapCoords(Vector2 pos) {
        int height = Planet.getPlanet().getSize().y;
        return new Vector2(pos.x, height - 1 - pos.y);
    }

    private Vector2 orientationToDirection(Object orientation) {
        String orient = orientation == null ? "" : orientation.toString().toLowerCase();
        return switch (orient) {
            case "north", "n", "up" -> new Vector2(0, 1);
            case "east", "e", "right" -> new Vector2(1, 0);
            case "south", "s", "down" -> new Vector2(0, -1);
            case "west", "w", "left" -> new Vector2(-1, 0);
            default -> new Vector2(0, 0);
        };
    }

    private void UpdateMap(Information info) {
        // effacer ancienne position du rover
        if (info == null || info.position == null) {
            // rien à faire si info invalide
            return;
        }

        // Si première réception, afficher la position initiale sans effacer
        if (lastRoverPos == null) {
            Vector2 initMapPos = toMapCoords(info.position);
            map.setCell(initMapPos, TypeElement.ROVER);
            lastRoverPos = initMapPos;
        }
        if (info.success) {
            Vector2 newMapPos = toMapCoords(info.position);
            // ne supprimer l'ancienne case que si le rover a réellement changé de case
            if (lastRoverPos == null || newMapPos.x != lastRoverPos.x || newMapPos.y != lastRoverPos.y) {
                if (lastRoverPos != null) {
                    map.setCell(lastRoverPos, TypeElement.EMPTY);
                }
                map.setCell(newMapPos, TypeElement.ROVER);
                lastRoverPos = newMapPos;
            }
        } else {
            // mouvement bloqué : ne pas effacer le rover, marquer l'obstacle devant
            Vector2 direction = orientationToDirection(info.orientation);
            Vector2 obstacleLogical = info.position.add(direction);

            int width = Planet.getPlanet().getSize().x;
            int height = Planet.getPlanet().getSize().y;
            if (obstacleLogical.x >= 0 && obstacleLogical.x < width && obstacleLogical.y >= 0 && obstacleLogical.y < height) {
                Vector2 obstacleMapPos = toMapCoords(obstacleLogical);
                // Ne pas écraser l'affichage du rover
                if (lastRoverPos == null || obstacleMapPos.x != lastRoverPos.x || obstacleMapPos.y != lastRoverPos.y) {
                    map.setCell(obstacleMapPos, TypeElement.OBSTACLE);
                }
            }
        }

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