package org.ynov.rover;
import org.ynov.communication.Information;
import org.ynov.communication.Instruction;

import java.util.Vector;

public interface IInstructable {
   public Information ProcessInstruction(Vector<Instruction> instruction);

}
