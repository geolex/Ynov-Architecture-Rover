package org.ynov.utils;

import org.ynov.communication.Connection;
import org.ynov.communication.ICommunicator;

public class FixedCommunicator implements ICommunicator {
    private final Connection toReturn;

    public FixedCommunicator(Connection toReturn) {
        this.toReturn = toReturn;
    }

    @Override
    public Connection HostCommunication() {
        return toReturn;
    }

    @Override
    public Connection ConnectToCommunication() {
        return toReturn;
    }

    @Override
    public void CloseCommunication(Connection connection) {
        try {
            if (connection != null) {
                if (connection.in != null) connection.in.close();
                if (connection.out != null) connection.out.close();
            }
        } catch (Exception ignored) {}
    }
}
