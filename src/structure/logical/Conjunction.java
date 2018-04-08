package structure.logical;

import structure.BinaryEntity;
import structure.Entity;
import structure.arithmetic.Constant;

/**
 * Created by Антон on 02.01.2017.
 */
public class Conjunction extends BinaryEntity {

    public Conjunction(Entity leftOperand, Entity rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + "&" + rightOperand.toString() + ")";
    }

    @Override
    public Entity newInstance() {
        return new Conjunction(leftOperand.newInstance(), rightOperand.newInstance());
    }
}