package structure;

public abstract class UnaryEntity implements Entity {
    protected final Entity operand;

    public UnaryEntity (Entity operand) {
        this.operand = operand;
    }
}