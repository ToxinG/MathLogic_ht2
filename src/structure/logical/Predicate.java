package structure.logical;

import structure.Entity;

import java.util.ArrayList;

/**
 * Created by Антон on 02.01.2017.
 */
public class Predicate implements Entity {
    public String name = "";
    public String type = "";
    public ArrayList<Entity> operands;

    @Override
    public String toString() {
        if (type.equals("=")) {
            return "(" + operands.get(0).toString() + "=" + operands.get(1).toString();
        } else if (operands.size() == 0) {
            return name;
        } else {
            String res = name;
            res += "(";
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
