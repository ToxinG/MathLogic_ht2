package structure;

public abstract class BinaryEntity implements Entity {
    public Entity leftOperand;
    public Entity rightOperand;

    public BinaryEntity (Entity leftOperand, Entity rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }
}
