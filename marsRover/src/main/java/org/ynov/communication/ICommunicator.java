package org.ynov.communication;

public interface ICommunicator {
    Connection HostCommunication();
    Connection ConnectToCommunication();
    void CloseCommunication(Connection connection);
}
