package structure.logical;

/**
 * Created by Антон on 02.01.2017.
 */
public abstract class UnaryLogicalEntity implements LogicalEntity {
    public final LogicalEntity operand;

    public UnaryLogicalEntity (LogicalEntity operand) {
        this.operand = operand;
    }
}
