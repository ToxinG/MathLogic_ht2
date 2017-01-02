package structure.logical;

/**
 * Created by Антон on 02.01.2017.
 */
public abstract class BinaryLogicalEntity implements LogicalEntity {
    public final LogicalEntity leftOperand;
    public final LogicalEntity rightOperand;

    public BinaryLogicalEntity (LogicalEntity leftOperand, LogicalEntity rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }
}
