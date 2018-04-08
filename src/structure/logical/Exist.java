package structure.logical;

import structure.Entity;
import structure.Variable;

public class Exist extends Quantifier {

    public Exist(Variable variable, Entity operand) {
        super(variable, operand);
    }

    @Override
    public String toString() {
        return "?" + variable.toString() + "(" + operand.toString() + ")";
    }

    @Override
    public Entity newInstance() {
        return new Exist((Variable) variable.newInstance(), operand.newInstance());
    }
}
