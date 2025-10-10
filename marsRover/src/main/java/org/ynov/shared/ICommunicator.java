package org.ynov.shared;

public interface ICommunicator {
    Connection HostCommunication();
    Connection ConnectToCommunication();
    void CloseCommunication(Connection connection);
}
