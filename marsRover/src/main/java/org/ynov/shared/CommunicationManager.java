package org.ynov.shared;

public class CommunicationManager {
    private static CommunicationManager instance;
    public final ICommunicator communicator;

    private CommunicationManager(){
        this.communicator = new CommunicatorTCP();
    }

    public static CommunicationManager Instance(){
        //System.out.printf("Accessing ");
        //if(instance == null){
        //    System.out.printf("inexistant");
        //}else{
        //    System.out.printf("existant");
        //}
        //System.out.printf(" Communication Manager");

        if(instance == null){
            instance = new CommunicationManager();
        }
        return instance;
    }
}
