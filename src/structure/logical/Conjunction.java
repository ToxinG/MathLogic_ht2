package structure.logical;

/**
 * Created by Антон on 02.01.2017.
 */
public class Conjunction extends BinaryLogicalEntity {

    public Conjunction(LogicalEntity leftOperand, LogicalEntity rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + "&" + rightOperand.toString() + ")";
    }
}