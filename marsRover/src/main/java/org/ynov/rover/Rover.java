// src/main/java/org/ynov/rover/Rover.java
package org.ynov.rover;

import lombok.Getter;
import org.ynov.shared.*;

import java.util.Vector;

public class Rover implements IInstructable {
    @Getter
    private Vector2 position;
    @Getter
    private OrientationEnum orientation;
    private Planet planet;

    public Rover(Planet planet, OrientationEnum orientation) {
        this.position = new Vector2(0, 0);
        this.orientation = orientation;
        this.planet = planet;
    }

    public boolean move(InstructionEnum instruction) {
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

        // Vérifie si la nouvelle position est valide et sans obstacle
        if (isValidPosition(x, y)) {
            position.x = x;
            position.y = y;
            return true;
        }
        return false;
    }

    private boolean isValidPosition(int x, int y) {
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

    @Override
    public Information ProcessInstruction(Vector<Instruction> instruction) {
        for (Instruction instr : instruction) {
            move(instr.instruction);
        }
        return new Information(position, true, orientation);
    }
}
