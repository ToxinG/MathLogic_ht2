package structure;

public abstract class UnaryEntity {
    public final Entity operand;

    public UnaryEntity (Entity operand) {
        this.operand = operand;
    }
}