package org.ynov.rover;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ynov.shared.Instruction;
import org.ynov.shared.OrientationEnum;
import org.ynov.shared.Vector2;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws Exception {
        // Création de la planète
        int taille = 10;
        Planet planet = new Planet("Mars", 10, 5);
        Rover rover = new Rover(OrientationEnum.North);

        // Affichage de l'état initial
        System.out.println("Position initiale du rover : " + rover.getPosition());
        System.out.println("Orientation initiale : " + rover.getOrientation());

        // Lancement de l'écoute et exécution des instructions
        rover.listenAndExecute();
    }
}
