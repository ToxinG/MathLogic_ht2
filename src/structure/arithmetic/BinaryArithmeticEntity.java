package structure.arithmetic;

/**
 * Created by Антон on 02.01.2017.
 */
public abstract class BinaryArithmeticEntity implements ArithmeticEntity {
    public final ArithmeticEntity leftOperand;
    public final ArithmeticEntity rightOperand;

    public BinaryArithmeticEntity (ArithmeticEntity leftOperand, ArithmeticEntity rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }
}
