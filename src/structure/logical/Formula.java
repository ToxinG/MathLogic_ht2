package structure.logical;

import structure.Entity;
import structure.UnaryEntity;

/**
 * Created by Антон on 02.01.2017.
 */
public class Formula extends UnaryEntity {

    private String type;
    private Entity variable;

    public Formula (String type, Entity variable, Entity operand) {
        super(operand);
        this.type = type;
        this.variable = variable;
    }

    @Override
    public String toString() {
        return type + variable.toString() + operand.toString();
    }
}
