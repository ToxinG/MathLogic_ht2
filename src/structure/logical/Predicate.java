package structure.logical;

import structure.Entity;

import java.util.ArrayList;

/**
 * Created by Антон on 02.01.2017.
 */
public class Predicate implements Entity {
    public String name;
    public ArrayList<Entity> operands;

    public Predicate(String name, ArrayList<Entity> operands) {
        this.name = name;
        this.operands = operands;
    }

    @Override
    public String toString() {
        if (operands.size() == 0) {
            return name;
        } else {
            String res = name;
            res += "(";
            for (int i = 0; i < operands.size() - 1; i++) {
                res += operands.get(i).toString();
                res += ",";
            }
            res += operands.get(operands.size() - 1);
            res += ")";
            return res;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Predicate)) {
            return false;
        }

        if (!(((Predicate) object).name.equals(name))) {
            return false;
        }

        if (!(((Predicate) object).operands.size() == operands.size())) {
            return false;
        }

        for (int i = 0; i < operands.size(); i++)
            if (!(operands.get(i).toString().
                    equals(((Predicate) object).operands.get(i).toString()))) {
                return false;
            }

        return true;
    }

    @Override
    public Entity newInstance() {
        return new Predicate(name, (ArrayList<Entity>) operands.clone());
    }
}
