package org.ynov.rover;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.ynov.shared.Information;
import org.ynov.shared.Instruction;
import org.ynov.shared.InstructionEnum;
import org.ynov.shared.OrientationEnum;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.ynov.shared.InstructionEnum.Forward;

public class RoverTest {

    @Nested
    class MoveTest {
        @Test
        void move() {
            //GIVEN
            Rover rover = new Rover(OrientationEnum.North);
            InstructionEnum instructionEnum = Forward;

            //WHEN
            boolean success = rover.move(instructionEnum);

            //THEN
            assertTrue(success, "Rover should successfully move forward.");
            assertEquals(0, rover.getPosition().x);
            assertEquals(1, rover.getPosition().y);
        }

        @Test
        void moveBackward() {
            //GIVEN
            Rover rover = new Rover(OrientationEnum.South);
            InstructionEnum instructionEnum = InstructionEnum.Backward;

            //WHEN
            boolean success = rover.move(instructionEnum);

            //THEN
            assertTrue(success, "Rover should successfully move backward.");
            assertEquals(0, rover.getPosition().x);
        }

        @Test
        void turnLeft() {
            //GIVEN
            Rover rover = new Rover(OrientationEnum.East);
            InstructionEnum instructionEnum = InstructionEnum.TurnLeft;

            //WHEN
            boolean success = rover.move(instructionEnum);

            //THEN
            assertTrue(success, "Rover should successfully turn left.");
            assertEquals(OrientationEnum.North, rover.getOrientation());
        }

        @Test
        void turnRight() {
            //GIVEN
            Rover rover = new Rover(OrientationEnum.West);
            InstructionEnum instructionEnum = InstructionEnum.TurnRight;

            //WHEN
            boolean success = rover.move(instructionEnum);

            //THEN
            assertTrue(success, "Rover should successfully turn right.");
            assertEquals(OrientationEnum.North, rover.getOrientation());
        }
    }

    @Nested
    class ListenAndExecuteTest {

        @Test
        void processInstruction_singleValidInstruction() {
            // GIVEN
            Rover rover = new Rover(OrientationEnum.North);
            Vector<Instruction> instructions = new Vector<>();
            instructions.add(new Instruction(InstructionEnum.Forward));

            // WHEN
            Information result = rover.ProcessInstruction(instructions);

            // THEN
            assertTrue(result.success, "Single valid instruction should succeed.");
            assertEquals(0, result.position.x);
            assertEquals(1, result.position.y);
            assertEquals(OrientationEnum.North, result.orientation);
        }

        @Test
        void processInstruction_multipleInstructions() {
            // GIVEN
            Rover rover = new Rover(OrientationEnum.North);
            Vector<Instruction> instructions = new Vector<>();
            instructions.add(new Instruction(InstructionEnum.Forward));
            instructions.add(new Instruction(InstructionEnum.TurnRight));
            instructions.add(new Instruction(InstructionEnum.Forward));

            // WHEN
            Information result = rover.ProcessInstruction(instructions);

            // THEN
            assertTrue(result.success, "Multiple valid instructions should succeed.");
            assertEquals(1, result.position.x);
            assertEquals(1, result.position.y);
            assertEquals(OrientationEnum.East, result.orientation);
        }
    }
}
