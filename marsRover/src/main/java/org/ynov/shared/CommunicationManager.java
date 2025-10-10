package org.ynov.shared;

public class CommunicationManager {
    private static CommunicationManager instance;
    public final ICommunicator communicator;

    private CommunicationManager(){
        this.communicator = new CommunicatorTCP();
    }

    public static CommunicationManager Instance(){
        if(instance == null){
            instance = new CommunicationManager();
        }
        return instance;
    }
}
