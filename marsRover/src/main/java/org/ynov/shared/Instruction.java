package org.ynov.shared;

import java.util.Vector;

public class Instruction {

    public final InstructionEnum instruction;

    public Instruction() {
        this.instruction = null;
    }
    public Instruction(InstructionEnum instruction) {
        this.instruction = instruction;
    }

    private static String EncodeOne(Instruction instruction){
        return instruction.instruction.toString();
    };

    public static String Encode(Vector<Instruction> instructions){
        String result = "";
        for(int i = 0; i < instructions.size(); i++){
            result += EncodeOne(instructions.get(i)) + ((i < instructions.size()-1)? "," : "");
        }
        return result;
    }

    private static Instruction DecodeOne(String data){
        Integer value = IntParser.TryIntParse(data);

        if(value == null || value < 0 || value > InstructionEnum.values().length){return null;}

        InstructionEnum instruction = InstructionEnum.values()[value];
        return new Instruction(instruction);
    };

    public static Vector<Instruction> Decode(String listData){
        Vector<Instruction> instructions = new Vector<Instruction>();
        for(String data : listData.split("-")){
            //TODO: Decide what to do if one instruction in a stream is invalid
            instructions.add(DecodeOne(data));
        }
        return instructions;
    }
}
