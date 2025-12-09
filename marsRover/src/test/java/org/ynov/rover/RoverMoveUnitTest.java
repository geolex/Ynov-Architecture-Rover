package org.ynov.rover;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.ynov.communication.Connection;
import org.ynov.communication.Instruction;
import org.ynov.communication.InstructionEnum;
import org.ynov.utils.FixedCommunicator;
import org.ynov.world.OrientationEnum;
import org.ynov.world.Planet;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Vector;


public class RoverMoveUnitTest {

    private static Connection dummyConnection() {
        // Connection avec flux vides pour satisfaire le constructeur mais non utilisés ici
        BufferedReader in = new BufferedReader(new StringReader(""));
        PrintWriter out = new PrintWriter(new StringWriter(), true);
        return new Connection(in, out);
    }

    @Test
    public void processInstructionVector() {
        Planet planet = new Planet("P", 5, 5);
        Rover rover = new Rover(OrientationEnum.North, new FixedCommunicator(dummyConnection()), planet);

        Vector<Instruction> instructions = new Vector<>();
        instructions.add(new Instruction(InstructionEnum.Forward));
        instructions.add(new Instruction(InstructionEnum.TurnRight));
        instructions.add(new Instruction(InstructionEnum.Forward));

        var info = rover.ProcessInstruction(instructions);
        Assertions.assertEquals(1, rover.getPosition().x);
        Assertions.assertEquals(4, rover.getPosition().y);
        Assertions.assertEquals(OrientationEnum.East, rover.getOrientation());
        Assertions.assertTrue(info.success);
    }
}
