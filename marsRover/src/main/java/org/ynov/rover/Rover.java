// src/main/java/org/ynov/rover/Rover.java
package org.ynov.rover;

import lombok.Getter;
import org.ynov.communication.*;
import org.ynov.world.*;

import java.io.IOException;
import java.util.Vector;

public class Rover extends Element implements IInstructable {

    @Getter
    private Vector2 position;
    @Getter
    private OrientationEnum orientation;
    private Connection connection;

    public Rover(OrientationEnum orientation, ICommunicator communicator) {
        super(TypeElement.ROVER);
        this.position = new Vector2(0, 0);
        this.orientation = orientation;
        this.connection = communicator.ConnectToCommunication();
    }

    public boolean move(InstructionEnum instruction) {
        Planet planet = Planet.getPlanet();
        int x = position.x;
        int y = position.y;
        int size = planet.getSize();

        switch (instruction) {
            case Forward:
                switch (orientation) {
                    case North: y++; break;
                    case South: y--; break;
                    case East:  x++; break;
                    case West:  x--; break;
                }
                break;
            case Backward:
                switch (orientation) {
                    case North: y--; break;
                    case South: y++; break;
                    case East:  x--; break;
                    case West:  x++; break;
                }
                break;
            case TurnLeft:
                orientation = turnLeft(orientation);
                return true;
            case TurnRight:
                orientation = turnRight(orientation);
                return true;
        }

        x = (x + size) % size;
        y = (y + size) % size;

        if (isValidPosition(x, y, planet)) {
            position.x = x;
            position.y = y;
            return true;
        }
        return false;
    }

    private boolean isValidPosition(int x, int y, Planet planet) {
        if (x < 0 || x >= planet.getSize() || y < 0 || y >= planet.getSize()) return false;
        for (Obstacle obs : planet.getObstacles()) {
            if (obs.getPosition().x == x && obs.getPosition().y == y) return false;
        }
        return true;
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



    @Override
    public Information ProcessInstruction(Vector<Instruction> instruction) {
        boolean success = true;
        for (Instruction instr : instruction) {
            boolean moveResult = move(instr.instruction);
            if (!moveResult) success = false;
        }
        return new Information(position, success, orientation);    }

}
