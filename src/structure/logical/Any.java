package structure.logical;

import structure.Entity;
import structure.Variable;

public class Any extends Quantifier {

    public Any(Variable variable, Entity operand) {
        super(variable, operand);
    }

    public String toString() {
        return "@" + variable.toString() + "(" + operand.toString() + ")";
    }
}
