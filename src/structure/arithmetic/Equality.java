package structure.arithmetic;

import structure.BinaryEntity;
import structure.Entity;

public class Equality extends BinaryEntity {

    public Equality(Entity leftOperand, Entity rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + "=" + rightOperand.toString() + ")";
    }

    @Override
    public Entity newInstance() {
        return new Equality(leftOperand.newInstance(), rightOperand.newInstance());
    }
}
