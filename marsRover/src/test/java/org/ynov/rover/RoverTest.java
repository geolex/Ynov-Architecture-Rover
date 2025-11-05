package org.ynov.rover;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.ynov.communication.Connection;
import org.ynov.communication.InstructionEnum;
import org.ynov.tcp.CommunicatorTCP;
import org.ynov.world.OrientationEnum;
import org.ynov.world.Planet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoverTest {

    @Nested
    class MoveTest {
        @Test
        void move() {
            //GIVEN
            Planet planet = new Planet("Mars",5, 5);
            Rover rover = new Rover(OrientationEnum.North, new CommunicatorTCP(), planet);

            // WHEN
            boolean moved = rover.move(InstructionEnum.Forward);

            // THEN
            assertTrue(moved);
            assertEquals(0, rover.getPosition().x);
            assertEquals(4, rover.getPosition().y);
            assertEquals(OrientationEnum.North, rover.getOrientation());
        }
        @Test
        void moveBackward () {
            //GIVEN
            Planet planet = new Planet("Mars", 5, 5);
            Rover rover = new Rover(OrientationEnum.North, new CommunicatorTCP(), planet);

            // WHEN
            boolean moved = rover.move(InstructionEnum.Backward);

            // THEN
            assertTrue(moved);
            assertEquals(0, rover.getPosition().x);
            assertEquals(1, rover.getPosition().y); // Moved one step south (y increases)
            assertEquals(OrientationEnum.North, rover.getOrientation());
        }

        @Test
        void turnLeft() {
            //GIVEN
            Planet planet = new Planet("Mars", 5, 5);
            Rover rover = new Rover(OrientationEnum.North, new CommunicatorTCP(), planet);

            // WHEN
            boolean turned = rover.move(InstructionEnum.TurnLeft);

            // THEN
            assertTrue(turned);
            assertEquals(OrientationEnum.West, rover.getOrientation());
        }

        @Test
        void turnRight() {
            //GIVEN
            Planet planet = new Planet("Mars", 5, 5);
            Rover rover = new Rover(OrientationEnum.North, new CommunicatorTCP(), planet);

            // WHEN
            boolean turned = rover.move(InstructionEnum.TurnRight);

            // THEN
            assertTrue(turned);
            assertEquals(OrientationEnum.East, rover.getOrientation());
        }
    }

    @Nested
    class ListenAndExecuteTest {

        @Test
        void listenAndExecute_validInstructions() throws IOException {
            // GIVEN
            Planet planet = new Planet("Mars", 5, 5);
            CommunicatorTCP mockCommunicator = new CommunicatorTCP();
//            mockConnection.in = new BufferedReader(new StringReader("F,B,L,R\n"));
//            mockConnection.out = new PrintWriter(new StringWriter(), true);
//            Connection mockConnection = new Connection();
//            mockCommunicator.setMockConnection(mockConnection);
            Rover rover = new Rover(OrientationEnum.North, mockCommunicator, planet);

            // WHEN
            rover.listenAndExecute();

            // THEN
            assertEquals(0, rover.getPosition().x);
            assertEquals(1, rover.getPosition().y);
            assertEquals(OrientationEnum.North, rover.getOrientation());
        }

        @Test
        void listenAndExecute_missingConnection() {
            // GIVEN
            Planet planet = new Planet("Mars", 5, 5);
            CommunicatorTCP mockCommunicator = new CommunicatorTCP(); // No connection will be established
            Rover rover = new Rover(OrientationEnum.North, mockCommunicator, planet);

            // WHEN / THEN (ensure no exception is thrown)
            rover.listenAndExecute();
        }

        @Test
        void listenAndExecute_invalidInstructions() throws IOException {
            // GIVEN
            Planet planet = new Planet("Mars", 5, 5);
            CommunicatorTCP mockCommunicator = new CommunicatorTCP();
            Connection mockConnection = new Connection();
            mockConnection.in = new BufferedReader(new StringReader("INVALID_COMMAND\n"));
            mockConnection.out = new PrintWriter(new StringWriter(), true);
            mockCommunicator.setMockConnection(mockConnection);
            Rover rover = new Rover(OrientationEnum.North, mockCommunicator, planet);

            // WHEN
            rover.listenAndExecute();

            // THEN
            assertEquals(0, rover.getPosition().x);
            assertEquals(0, rover.getPosition().y); // No movement expected
            assertEquals(OrientationEnum.North, rover.getOrientation());
        }

    }
}
