package org.ynov.missionControl;

import org.ynov.shared.CommunicationManager;
import org.ynov.shared.Connection;
import org.ynov.shared.Instruction;
import org.ynov.shared.InstructionEnum;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Vector;

public class MissionControl implements KeyListener {

    int[][] map;
    static boolean isPromptingUser = false;
    Vector<Instruction> currentInstructions = new Vector<Instruction>();
    Connection connection;

    public void Initialize(){
        System.out.println("Welcome to Kerbal's Mars Rover Program");

        connection = CommunicationManager.Instance().communicator.HostCommunication();

        PromptUser();
    }

    private void PromptUser(){
        System.out.println("What are your instructions?");
        isPromptingUser = true;
        ReadString();
    }

    private void ReadString(){
        int input;
        while (true) {
            try {
                if (!((input = System.in.read()) != '\n')) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.print((char) input);

            currentInstructions.add(new Instruction(switch (input) {
                case 'z' -> InstructionEnum.Forward;
                case 's' -> InstructionEnum.Backward;
                case 'q' -> InstructionEnum.TurnLeft;
                case 'd' -> InstructionEnum.TurnRight;
                default -> null;
            }));
        }
        connection.out.println(Instruction.Encode(currentInstructions));
        isPromptingUser = false;
    }



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!isPromptingUser) return;
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
           connection.out.println(Instruction.Encode(currentInstructions));
           System.out.println(connection.out.toString());
           isPromptingUser = false;
        }

        InstructionEnum instEnum = switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> InstructionEnum.Forward;
            case KeyEvent.VK_DOWN -> InstructionEnum.Backward;
            case KeyEvent.VK_LEFT -> InstructionEnum.TurnLeft;
            case KeyEvent.VK_RIGHT -> InstructionEnum.TurnRight;
            default -> null;
        };

        if(instEnum != null) currentInstructions.add(new Instruction(instEnum));
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
