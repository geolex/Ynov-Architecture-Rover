package org.ynov.missionControl;

import org.ynov.shared.Instruction;
import org.ynov.shared.InstructionEnum;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;
import java.util.Vector;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main implements KeyListener {

    int[][] map;
    static boolean isPromptingUser = false;
    Vector<Instruction> currentInstructions = new Vector<Instruction>();

    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Welcome to Kerbal's Mars Rover Program");

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
            SendInstruction();
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

    private void SendInstruction(){
        isPromptingUser = false;
    }

    public void ReceiveInformation(){
        

        PromptUser();
    }
}
