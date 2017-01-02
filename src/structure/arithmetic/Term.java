package structure.arithmetic;

/**
 * Created by Антон on 02.01.2017.
 */
public class Term extends UnaryArithmeticEntity {

    Term(ArithmeticEntity operand) {
        super(operand);
    }

    @Override
    public String toString() {
        return operand.toString();
    }
}
