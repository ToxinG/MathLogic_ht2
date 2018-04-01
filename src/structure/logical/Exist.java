package structure.logical;

import structure.Entity;
import structure.Variable;

public class Exist extends Quantifier {

    public Exist(Variable variable, Entity operand) {
        super(variable, operand);
    }

    public String toString() {
        return "@" + variable.toString() + "(" + operand.toString() + ")";
    }
}
