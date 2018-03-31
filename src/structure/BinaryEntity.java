package structure;

public abstract class BinaryEntity implements Entity {
    public final Entity leftOperand;
    public final Entity rightOperand;

    public BinaryEntity (Entity leftOperand, Entity rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }
}
