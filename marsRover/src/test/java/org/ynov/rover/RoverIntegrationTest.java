package org.ynov.rover;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.ynov.communication.Connection;
import org.ynov.utils.FixedCommunicator;
import org.ynov.utils.InMemoryConnectionFactory;
import org.ynov.world.Planet;
import org.ynov.world.OrientationEnum;
import java.io.IOException;
import java.util.concurrent.*;

public class RoverIntegrationTest {

    @Test
    public void handshakeIsSentOnConstruction() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        InMemoryConnectionFactory.Pair pair = InMemoryConnectionFactory.createPair();
        Connection serverConn = pair.connA; // simulation MissionControl side
        Connection clientConn = pair.connB; // simulattion Rover side

        // Rover -> ConnectToCommunication(.
        FixedCommunicator communicator = new FixedCommunicator(clientConn);

        Planet planet = new Planet("Test", 5, 5);

        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<String> readFuture = exec.submit(() -> {
            return serverConn.in.readLine();
        });


        Rover rover = new Rover(OrientationEnum.North, communicator, planet);

        String handshake = readFuture.get(2, TimeUnit.SECONDS);

        exec.shutdownNow();

        Assertions.assertNotNull(handshake, "Le handshake ne doit pas être null");
        Assertions.assertFalse(handshake.trim().isEmpty(), "Le handshake ne doit pas être vide");
    }
}
