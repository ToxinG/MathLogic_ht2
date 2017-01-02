package structure.logical;

/**
 * Created by Антон on 02.01.2017.
 */
public class Implication extends BinaryLogicalEntity {

    public Implication(LogicalEntity leftOperand, LogicalEntity rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + "->" + rightOperand.toString() + ")";
    }
}