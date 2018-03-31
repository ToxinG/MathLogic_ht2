package structure.logical;

import structure.BinaryEntity;
import structure.Entity;

/**
 * Created by Антон on 02.01.2017.
 */
public class Disjunction extends BinaryEntity {

    public Disjunction(Entity leftOperand, Entity rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + "|" + rightOperand.toString() + ")";
    }
}
