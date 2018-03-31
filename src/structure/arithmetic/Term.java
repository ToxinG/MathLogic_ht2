package structure.arithmetic;

import structure.Entity;
import structure.UnaryEntity;

/**
 * Created by Антон on 02.01.2017.
 */
public class Term extends UnaryEntity {

    Term(Entity operand) {
        super(operand);
    }

    @Override
    public String toString() {
        return operand.toString();
    }
}
