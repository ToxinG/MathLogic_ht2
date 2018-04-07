package structure.arithmetic;

import structure.Entity;

import java.util.ArrayList;

/**
 * Created by Антон on 02.01.2017.
 */
public class Function implements Entity {
    public String name;
    public ArrayList<Entity> operands;

    public Function(String name, ArrayList<Entity> operands) {
        this.name = name;
        this.operands = operands;
    }

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
