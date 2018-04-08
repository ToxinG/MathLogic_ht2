package structure.logical;

import structure.Entity;
import structure.UnaryEntity;
import structure.Variable;

/**
 * Created by Антон on 02.01.2017.
 */
public abstract class Quantifier implements Entity {

    public Variable variable;
    public Entity operand;

    public Quantifier(Variable variable, Entity operand) {
        this.variable = variable;
        this.operand = operand;
    }
}
