package structure.arithmetic;

import structure.BinaryEntity;
import structure.Entity;

/**
 * Created by Антон on 02.01.2017.
 */
public class Sum extends BinaryEntity {

    public Sum(Entity leftOperand, Entity rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + "+" + rightOperand.toString() + ")";
    }
}