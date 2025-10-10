// src/main/java/org/ynov/rover/Rover.java
package org.ynov.rover;

import lombok.Getter;
import org.ynov.shared.*;
import java.io.IOException;
import java.util.Vector;

public class Rover extends Element implements IInstructable {

    @Getter
    private Vector2 position;
    @Getter
    private OrientationEnum orientation;
    private Connection connection;

    public Rover(OrientationEnum orientation) {
        super(TypeElement.ROVER);
        this.position = new Vector2(0, 0);
        this.orientation = orientation;
        this.connection = CommunicationManager.Instance().communicator.ConnectToCommunication();
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

    // src/main/java/org/ynov/rover/Rover.java
    public void listenAndExecute() {
        try {
            String data;
            while ((data = connection.in.readLine()) != null) {
                System.out.println(data);
                Vector<Instruction> instructions = Instruction.Decode(data);
                Information info = ProcessInstruction(instructions);
                connection.out.println(info.toString());
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