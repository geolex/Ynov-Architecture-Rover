package org.ynov.shared;

import java.net.ServerSocket;

public interface ICommunicator {
    Connection OpenCommunication();
    void CloseCommunication(Connection connection);
}
