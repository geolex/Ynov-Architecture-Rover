package org.ynov.rover;

import org.ynov.tcp.CommunicatorTCP;
import org.ynov.world.OrientationEnum;
import org.ynov.world.Planet;


public class Main {

    public static void main(String[] args) throws Exception {
        // Création de la planète
        int taille = 10;
        Planet planet = new Planet("Mars", 4, 5);
        Rover rover = new Rover(OrientationEnum.North, new CommunicatorTCP(), planet);

        // Affichage de l'état initial
        System.out.println("Position initiale du rover : " + rover.getPosition());
        System.out.println("Orientation initiale : " + rover.getOrientation());

        // Lancement de l'écoute et exécution des instructions
        rover.listenAndExecute();
    }
}
