package structure.logical;

import structure.Entity;
import structure.UnaryEntity;
import structure.Variable;

/**
 * Created by Антон on 02.01.2017.
 */
public abstract class Quantifier extends UnaryEntity {

    protected Variable variable;

    public Quantifier(Variable variable, Entity operand) {
        super(operand);
        this.variable = variable;
    }
}
