package org.ynov.shared;

public class Instruction {
    public final InstructionEnum instruction;

    public Instruction(InstructionEnum instruction) {
        this.instruction = instruction;
    }

    public static String Encode(InstructionEnum instruction){
        return instruction.toString();
    };

    public static Instruction Decode(String data){
        Integer value = IntParser.TryIntParse(data);

        if(value == null || value < 0 || value > InstructionEnum.values().length){return null;}

        InstructionEnum instruction = InstructionEnum.values()[value];
        return new Instruction(instruction);
    };
}
