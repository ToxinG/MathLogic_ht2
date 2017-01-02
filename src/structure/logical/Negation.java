package structure.logical;

/**
 * Created by Антон on 02.01.2017.
 */
public class Negation extends UnaryLogicalEntity {

    Negation(LogicalEntity operand) {
        super(operand);
    }

    @Override
    public String toString() {
        return "!" + operand.toString();
    }
}
