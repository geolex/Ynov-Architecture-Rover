// java
package org.ynov.rover;

import lombok.Getter;
import org.ynov.communication.*;
import org.ynov.world.*;

import java.io.IOException;
import java.util.Vector;

public class Rover extends Element {
    @Getter
    private Vector2 position;
    @Getter
    private OrientationEnum orientation;
    private Connection connection;
    private final Planet planet;
    private final PlanetFrame planetFrame; // Référence à la fenêtre

    public Rover(OrientationEnum orientation, ICommunicator communicator, Planet planet, PlanetFrame planetFrame) {
        super(TypeElement.ROVER);
        this.position = new Vector2(0, 0);
        this.orientation = orientation;
        this.connection = communicator.ConnectToCommunication();
        this.planet = planet;
        this.planetFrame = planetFrame;
        planetFrame.view();

        // Envoi du handshake initial (position + orientation) immédiatement après connexion
        if (this.connection != null && this.connection.out != null) {
            try {
                Information initialInfo = new Information(position, true, orientation);
                this.connection.out.println(Information.Encode(initialInfo));
                this.connection.out.flush();
                System.out.println("Handshake initial envoyé au Mission Control");
            } catch (Exception e) {
                System.err.println("Impossible d'envoyer le handshake initial: " + e.getMessage());
            }
        } else {
            System.err.println("Flux de sortie indisponible : handshake initial non envoyé.");
        }
    }

    public boolean move(InstructionEnum instruction) {
        int x = position.x;
        int y = position.y;

        switch (instruction) {
            case Forward:
                switch (orientation) {
                    // Nord -> remonter dans la console : y--
                    case North: y--; break;
                    case South: y++; break;
                    case East:  x++; break;
                    case West:  x--; break;
                }
                break;
            case Backward:
                // backward = inverse de forward
                switch (orientation) {
                    case North: y++; break;
                    case South: y--; break;
                    case East:  x--; break;
                    case West:  x++; break;
                }
                break;
            case TurnLeft:
                orientation = turnLeft(orientation);
                updateFrame();
                return true;
            case TurnRight:
                orientation = turnRight(orientation);
                updateFrame();
                return true;
        }

        x = (x + planet.getSize().x) % planet.getSize().x;
        y = (y + planet.getSize().y) % planet.getSize().y;

        if (isValidPosition(x, y)) {
            position.x = x;
            position.y = y;
            updateFrame(); // Mise à jour de l'interface graphique
            return true;
        }
        return false;
    }

    private boolean isValidPosition(int x, int y) {
        if (x < 0 || x >= planet.getSize().x || y < 0 || y >= planet.getSize().y) return false;
        return planet.GetElement(new Vector2(x, y)) == null;
    }

    private OrientationEnum turnLeft(OrientationEnum ori) {
        switch (ori) {
            case North: return OrientationEnum.West;
            case West:  return OrientationEnum.South;
            case South: return OrientationEnum.East;
            case East:  return OrientationEnum.North;
        }
        return ori;
    }

    private OrientationEnum turnRight(OrientationEnum ori) {
        switch (ori) {
            case North: return OrientationEnum.East;
            case East:  return OrientationEnum.South;
            case South: return OrientationEnum.West;
            case West:  return OrientationEnum.North;
        }
        return ori;
    }

    public void listenAndExecute() {
        if (connection == null) {
            System.err.println("Connection non établie : impossible d'écouter ou d'envoyer la position initiale.");
            return;
        }
        if (connection.out == null || connection.in == null) {
            System.err.println("Flux de communication manquant (in/out).");
            return;
        }

        try {
            while (true) {
                String last = connection.in.readLine();
                if (last == null) break;

                while (connection.in.ready()) {
                    String next = connection.in.readLine();
                    if (next == null) break;
                }

                System.out.println(last + "-> commande recu");
                Vector<Instruction> instructions = Instruction.Decode(last);
                Information info = ProcessInstruction(instructions);
                connection.out.println(Information.Encode(info));
                connection.out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Information ProcessInstruction(Vector<Instruction> instruction) {
        boolean success = true;
        for (Instruction instr : instruction) {
            boolean moveResult = move(instr.instruction);
            if (!moveResult) success = false;
        }
        return new Information(position, success, orientation);
    }

    // Méthode pour rafraîchir la fenêtre après chaque changement d'état
    private void updateFrame() {
        if (planetFrame != null) {
            planetFrame.update(); // Actualise l'affichage
        }
    }
}
