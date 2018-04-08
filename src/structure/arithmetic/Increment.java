package structure.arithmetic;

import structure.Entity;
import structure.UnaryEntity;

/**
 * Created by Антон on 02.01.2017.
 */
public class Increment extends UnaryEntity {

    public Increment(Entity operand) {
        super(operand);
    }

    @Override
    public String toString() {
        return operand.toString() + "'";
    }

    @Override
    public Entity newInstance() {
        return new Increment(operand.newInstance());
    }
}
