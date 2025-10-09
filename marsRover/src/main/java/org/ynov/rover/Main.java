package org.ynov.rover;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ynov.shared.Instruction;
import org.ynov.shared.OrientationEnum;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws Exception {
        String json = "{\"instruction\":\"Forward\"}"; // Exemple de JSON reçu

        ObjectMapper mapper = new ObjectMapper();
        Instruction instruction = mapper.readValue(json, Instruction.class);

        // Création de la planète
        Planet planet = new Planet("Mars", 10, 5); // nom, taille, nombre d'obstacles

        // Création du rover sur la planète
        Rover rover = new Rover(planet, OrientationEnum.North);
        rover.move(instruction.instruction);

       System.out.println("Nouvelle position : " + rover.getPosition().x + ", " + rover.getPosition().y);
       System.out.println("Orientation : " + rover.getOrientation());
    }
}
