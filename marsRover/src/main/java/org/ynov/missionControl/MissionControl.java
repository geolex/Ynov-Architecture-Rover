package org.ynov.missionControl;

import org.ynov.shared.CommunicationManager;
import org.ynov.shared.Connection;
import org.ynov.shared.Instruction;
import org.ynov.shared.InstructionEnum;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

public class MissionControl implements KeyListener {

    int[][] map;
    static boolean isPromptingUser = false;
    Vector<Instruction> currentInstructions = new Vector<Instruction>();
    Connection connection;

    public void Initialize(){
        System.out.printf("Welcome to Kerbal's Mars Rover Program");

        connection = CommunicationManager.Instance().communicator.OpenCommunication();

        PromptUser();
    }

    private void PromptUser(){
        System.out.printf("What are your instructions?");
        isPromptingUser = true;
    }



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!isPromptingUser) return;
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
           connection.out.println(Instruction.Encode(currentInstructions));
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
