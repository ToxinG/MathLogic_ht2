package structure.arithmetic;

/**
 * Created by Антон on 02.01.2017.
 */
public abstract class UnaryArithmeticEntity implements ArithmeticEntity {
    public final ArithmeticEntity operand;

    public UnaryArithmeticEntity (ArithmeticEntity operand) {
        this.operand = operand;
    }
}
