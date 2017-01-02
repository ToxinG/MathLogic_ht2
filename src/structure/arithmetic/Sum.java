package structure.arithmetic;

/**
 * Created by Антон on 02.01.2017.
 */
public class Sum extends BinaryArithmeticEntity {

    Sum(ArithmeticEntity leftOperand, ArithmeticEntity rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + "+" + rightOperand.toString() + ")";
    }
}