package structure.arithmetic;

/**
 * Created by Антон on 02.01.2017.
 */
public class Multiplication extends BinaryArithmeticEntity {

    Multiplication(ArithmeticEntity leftOperand, ArithmeticEntity rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + "*" + rightOperand.toString() + ")";
    }
}
