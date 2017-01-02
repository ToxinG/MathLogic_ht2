package structure.logical;

import structure.arithmetic.ArithmeticEntity;

/**
 * Created by Антон on 02.01.2017.
 */
public class Formula extends UnaryLogicalEntity {

    private String type;
    private ArithmeticEntity variable;

    Formula (String type, ArithmeticEntity variable, UnaryLogicalEntity operand) {
        super(operand);
        this.type = type;
        this.variable = variable;
    }

    @Override
    public String toString() {
        return type + variable.toString() + operand.toString();
    }
}
