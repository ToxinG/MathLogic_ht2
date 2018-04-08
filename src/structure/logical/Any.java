package structure.logical;

import structure.Entity;
import structure.Variable;

public class Any extends Quantifier {

    public Any(Variable variable, Entity operand) {
        super(variable, operand);
    }

    @Override
    public String toString() {
        return "@" + variable.toString() + "(" + operand.toString() + ")";
    }

    @Override
    public Entity newInstance() {
        return new Any((Variable) variable.newInstance(), operand.newInstance());
    }
}
