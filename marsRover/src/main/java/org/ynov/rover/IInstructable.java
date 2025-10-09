package org.ynov.rover;
import org.ynov.shared.Information;
import  org.ynov.shared.Instruction;

import java.util.Vector;

public interface IInstructable {
   public Information ProcessInstruction(Vector<Instruction> instruction);

}
