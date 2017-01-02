package structure.arithmetic;

import java.util.ArrayList;

/**
 * Created by Антон on 02.01.2017.
 */
public class Function implements ArithmeticEntity {
    public String name;
    public ArrayList<ArithmeticEntity> operands;

    @Override
    public String toString() {
        if (operands.size() == 0) {
            return name;
        } else {
            String res = name;
            name += "(";
            for (int i = 0; i < operands.size() - 1; i++) {
                res += operands.get(i).toString();
                res += ", ";
            }
            res += operands.get(operands.size() - 1);
            res += ")";
            return res;
        }
    }
}
