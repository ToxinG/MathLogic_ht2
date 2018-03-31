package structure.logical;

import structure.Entity;
import structure.UnaryEntity;

/**
 * Created by Антон on 02.01.2017.
 */
public class Negation extends UnaryEntity {

    public Negation(Entity operand) {
        super(operand);
    }

    @Override
    public String toString() {
        return "!" + operand.toString();
    }
}
