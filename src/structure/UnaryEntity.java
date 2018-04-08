package structure;

public abstract class UnaryEntity implements Entity {
    public Entity operand;

    public UnaryEntity (Entity operand) {
        this.operand = operand;
    }
}