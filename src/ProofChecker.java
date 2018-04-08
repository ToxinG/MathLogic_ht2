import structure.*;
import structure.logical.*;
import structure.arithmetic.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by Антон on 06.01.2017.
 */
public class ProofChecker {

    public boolean changing;
    public boolean any;
    public boolean anyHyp;
    public boolean exist;
    public boolean existHyp;

    public boolean entityEquality (Entity first, Entity second) {
        if (first == null || second == null) return false;
        if (first.getClass() == second.getClass()) {
            if (first instanceof Variable)
                return ((Variable) first).name.equals(((Variable) second).name);

            if (first instanceof BinaryEntity)
                return entityEquality(((BinaryEntity) first).leftOperand, ((BinaryEntity) second).leftOperand)
                        && entityEquality(((BinaryEntity) first).rightOperand, ((BinaryEntity) second).rightOperand);

            if (first instanceof UnaryEntity)
                return entityEquality(((UnaryEntity) first).operand, ((UnaryEntity) second).operand);

            if (first instanceof Exist)
                return entityEquality(((Exist) first).variable, ((Exist) second).variable) &&
                        entityEquality(((Exist) first).operand, ((Exist) second).operand);

            if (first instanceof Any)
                return entityEquality(((Any) first).operand, ((Any) second).operand) &&
                        entityEquality(((Any) first).variable, ((Any) second).variable);

            if (first instanceof Predicate)
                return ((Predicate) first).name.equals(((Predicate) second).name) &&
                        listEquality(((Predicate) first).operands, ((Predicate) second).operands);

            if (first instanceof Function)
                return ((Function) first).name.equals(((Function) second).name) &&
                        listEquality(((Function) first).operands, ((Function) second).operands);

            if (first instanceof Constant)
                return ((Constant) first).name.equals(((Constant) second).name);

        }
        return false;
    }

    public boolean entityConformity (Entity first, Entity second, HashMap<String, Entity> namesMap) {
        if (first == null || second == null) return false;

        if (first.getClass() == second.getClass()) {
            if (first instanceof BinaryEntity)
                return entityConformity(((BinaryEntity) first).leftOperand,
                        ((BinaryEntity) second).leftOperand, namesMap) &&
                        entityConformity(((BinaryEntity) first).rightOperand,
                                ((BinaryEntity) second).rightOperand, namesMap);

            if (first instanceof Quantifier)
                return entityConformity(((Quantifier) first).variable, ((Quantifier) second).variable, namesMap) &&
                        entityConformity(((Quantifier) first).operand, ((Quantifier) second).operand, namesMap);

            if (first instanceof Predicate) {
                if (((Predicate) first).operands.size()
                        != ((Predicate) second).operands.size() ||
                        !(((Predicate) first).name.
                                equals(((Predicate) second).name))) return false;
                boolean flag = true;
                for (int i = 0; i < ((Predicate) first).operands.size(); i++) {
                    flag = entityConformity(((Predicate) first).operands.get(i),
                            ((Predicate) second).operands.get(i), namesMap);
                    if (!flag)
                        break;
                }
                return flag;
            }

            if (first instanceof Function) {
                if (((Function) first).operands.size() != ((Function) second).operands.size() ||
                        !(((Function) first).name.equals(((Function) second).name))) return false;
                boolean flag = true;
                for (int i = 0; i < ((Function) first).operands.size(); i++) {
                    flag = entityConformity(((Function) first).operands.get(i),
                            ((Function) second).operands.get(i), namesMap);
                    if (!flag)
                        break;
                }
                return flag;
            }

            if (first instanceof UnaryEntity)
                return entityConformity(((UnaryEntity) first).operand, ((UnaryEntity) second).operand, namesMap);

            if (first instanceof Constant)
                return ((Constant) first).name.equals(((Constant) second).name);

            if (first instanceof Variable) {
                if (((Variable) first).name.equals(((Variable) second).name)) {
                    if (!namesMap.containsKey(((Variable) second).name))
                        namesMap.put(((Variable) first).name, second);
                    else
                        return namesMap.get(((Variable) second).name).toString().equals(((Variable) first).name);
                    return true;
                }

                String tempName = ((Variable) second).name;
                if (namesMap.containsKey(tempName))
                    return entityEquality(first, namesMap.get(tempName));
                namesMap.put(tempName, first);
                return true;
            }
        }
        if (second instanceof Variable) {
            String tempName = ((Variable) second).name;
            if (namesMap.containsKey(tempName))
                return entityEquality(first, namesMap.get(tempName));
            namesMap.put(tempName, first);
            return true;
        }
        return false;

    }

    private boolean listEquality (ArrayList<Entity> first, ArrayList<Entity> second) {
        if (first.size() != second.size())
            return false;

        for (int i = 0; i < first.size(); i++)
            if (!(first.get(i).toString().equals(second.get(i).toString())))
                return false;

        return true;
    }

    public int checkAxioms(Entity e, Entity[] axioms) {
        HashMap<String, Entity> axiomMap;
        for (int i = 1; i < axioms.length; i++) {
            axiomMap = new HashMap<>();
            if (entityConformity(e, axioms[i], axiomMap))
                return i;
        }

        //11 axiom
        axiomMap = new HashMap<>();
        if (e instanceof Implication && ((Implication) e).leftOperand instanceof Any &&
                entityConformity(((Implication) e).rightOperand,
                        ((Any) ((Implication) e).leftOperand).operand, axiomMap)) {
            Entity eCopy = e.newInstance();
            Entity eSub = substitution(((Implication) e).leftOperand, ((Any) ((Implication) e).leftOperand).variable,
                    axiomMap.get(((Any) ((Implication) e).leftOperand).variable.name));
            e = eCopy;
            if (entityEquality((((Implication) e).rightOperand), ((Any) eSub).operand)) {
                Entity expr = axiomMap.get(((Any) ((Implication) e).leftOperand).variable.name);
                if (entityEquality(expr, ((Any) ((Implication) e).leftOperand).variable) ||
                        checkQuantifier(((Any) ((Implication) e).leftOperand).operand,
                                ((Any) ((Implication) e).leftOperand).variable, false, false, expr))
                    return 11;
                else
                    changing = true;
            }
        }

        //12 axiom
        axiomMap = new HashMap<>();
        if (e instanceof Implication && ((Implication) e).rightOperand instanceof Exist &&
                entityConformity(((Implication) e).leftOperand,
                        ((Exist) ((Implication) e).rightOperand).operand, axiomMap)) {
            Entity eCopy = e.newInstance();
            Entity eSub = substitution(((Implication) e).rightOperand, ((Exist) ((Implication) e).rightOperand).variable,
                    axiomMap.get(((Exist) ((Implication) e).rightOperand).variable.name));
            e = eCopy;
            if (entityEquality((((Implication) e).leftOperand), ((Exist) eSub).operand)) {
                Entity expr = axiomMap.get(((Exist) ((Implication) e).rightOperand).variable.name);
                if (entityEquality(expr, ((Exist) ((Implication) e).rightOperand).variable) ||
                        checkQuantifier(((Exist) ((Implication) e).rightOperand).operand,
                                ((Exist) ((Implication) e).rightOperand).variable, false, false, expr))
                    return 12;
                else
                    changing = true;
            }
        }

        return -1;
    }

    public Entity substitution(Entity expr, Variable var, Entity sub) {
        if (expr instanceof BinaryEntity) {
            ((BinaryEntity) expr).leftOperand = substitution(((BinaryEntity) expr).leftOperand, var, sub);
            ((BinaryEntity) expr).rightOperand = substitution(((BinaryEntity) expr).rightOperand, var, sub);
            return expr;
        }
        if (expr instanceof UnaryEntity) {
            ((UnaryEntity) expr).operand = substitution(((UnaryEntity) expr).operand, var, sub);
            return expr;
        }
        if (expr instanceof Quantifier) {
            entityEquality(((Quantifier) expr).variable, sub);
            ((Quantifier) expr).operand = substitution(((Quantifier) expr).operand, var, sub);
            return expr;
        }
        if (expr instanceof Predicate) {
            ((Predicate) expr).operands = ((Predicate) expr).operands.stream().map(e -> substitution(e, var, sub))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        if (expr instanceof Function) {
            ArrayList<Entity> newVars = new ArrayList<>();
            for (int i = 0; i < ((Function) expr).operands.size(); i++)
                newVars.add(substitution(((Function) expr).operands.get(i), var, sub));
            ((Function) expr).operands = newVars;
        }
        if (expr instanceof Variable)
            if (entityEquality(expr, var))
                return sub;

        return expr;
    }

    public boolean checkQuantifier(Entity e1, Variable var, boolean flag1, boolean flag2, Entity e2) {
        if (e1 instanceof Variable && entityEquality(e1, var))
            return flag1 || !flag2;

        if (e1 instanceof Quantifier && entityEquality(((Quantifier) e1).variable, e2))
            return checkQuantifier(((Quantifier) e1).operand, var, flag1, true, e2);

        if (e1 instanceof Quantifier && entityEquality(((Quantifier) e1).variable, var))
            return true;

        if (e1 instanceof BinaryEntity)
            return checkQuantifier(((BinaryEntity) e1).leftOperand, var, flag1, flag2, e2) &&
                    checkQuantifier(((BinaryEntity) e1).rightOperand, var, flag1, flag2, e2);

        if (e1 instanceof UnaryEntity)
            return (checkQuantifier(((UnaryEntity) e1).operand, var, flag1, flag2, e2));

        if (e1 instanceof Predicate)
            return (!((Predicate) e1).operands.contains(var)) || flag1 || !flag2;

        return !(e1 instanceof Function) || ((Function) e1).operands.contains(var) && !flag1 && flag2;
    }

    public boolean checkAxiomA9(Entity e) {
        if (e instanceof Implication && ((Implication) e).leftOperand instanceof Conjunction &&
                ((Conjunction) ((Implication) e).leftOperand).rightOperand instanceof Any) {
            HashMap<String, Entity> mapOfExp = new HashMap<>();
            if (entityConformity(((Conjunction) ((Implication) e).leftOperand).leftOperand,
                    ((Implication) e).rightOperand, mapOfExp)) {
                Variable v = ((Any) ((Conjunction) ((Implication) e).leftOperand).rightOperand).variable;
                if (mapOfExp.get(v.name) instanceof Constant) {
                    Entity tempExpr = e.newInstance();
                    Entity sub = substitution(((Implication) tempExpr).rightOperand, v, mapOfExp.get(v.name));
                    if (entityEquality(sub, ((Conjunction) ((Implication) tempExpr).leftOperand).leftOperand)) {
                        mapOfExp.clear();
                        Entity temp = ((Any) ((Conjunction) ((Implication) tempExpr).leftOperand).
                                rightOperand).operand;
                        if (temp instanceof Implication && entityConformity(((Implication) temp).rightOperand,
                                ((Implication) temp).leftOperand, mapOfExp))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkInstances(Variable x, Entity e, boolean quant) {
        if (e instanceof Variable && entityEquality(e, x))
            return quant;

        if (e instanceof Quantifier)
            return entityEquality(((Quantifier) e).variable, x) ||
                    checkInstances(x, ((Quantifier) e).operand, quant);

        if (e instanceof BinaryEntity)
            return checkInstances(x, ((BinaryEntity) e).leftOperand, quant) &&
                    checkInstances(x, ((BinaryEntity) e).rightOperand, quant);

        if (e instanceof UnaryEntity)
            return (checkInstances(x, ((UnaryEntity) e).operand, quant));

        if (e instanceof Predicate) {
            boolean flag = false;
            for (int i = 0; i < ((Predicate) e).operands.size(); i++) {
                if (!(checkInstances(x, ((Predicate) e).operands.get(i), quant))) {
                    flag = true;
                    break;
                }
            }
            return !flag || quant;
        }
        if (e instanceof Function) {
            boolean flag = false;
            for (int i = 0; i < ((Function) e).operands.size(); i++) {
                if (!(checkInstances(x, ((Function) e).operands.get(i), quant))) {
                    flag = true;
                    break;
                }
            }
            return !flag || quant;
        }
        return true;
    }

    public int checkAny(Entity e, ArrayList<Entity> exprList, ArrayList<Entity> hypotheses) {
        int flag = -1;
        if (!(e instanceof Implication)) return flag;
        for (int i = exprList.size() - 1; i >= 0; i--) {
            Entity e1 = exprList.get(i);
            if (e1 instanceof Implication && entityEquality(((Implication) e).leftOperand,
                    ((Implication) e1).leftOperand)) {
                if (((Implication) e).rightOperand instanceof Any && entityEquality(((Implication) e1).rightOperand,
                        ((Any) ((Implication) e).rightOperand).operand)) {
                    any = true;
                    if (checkInstances(((Any) ((Implication) e).rightOperand).variable,
                            ((Implication) e).leftOperand, false)) {
                        if (!hypotheses.isEmpty()) {
                            if (!checkInstances(((Any) ((Implication) e).rightOperand).variable,
                                    hypotheses.get(hypotheses.size() - 1), false)) {
                                anyHyp = true;
                                return -1;
                            }
                        }
                        flag = i;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    public int checkExist(Entity e, ArrayList<Entity> exprList, ArrayList<Entity> hypotheses) {
        int flag = -1;
        if (!(e instanceof Implication)) return flag;
        for (int i = exprList.size() - 1; i >= 0; i--) {
            Entity e1 = exprList.get(i);
            if (e1 instanceof Implication && entityEquality(((Implication) e).rightOperand,
                    ((Implication) e1).rightOperand)) {
                if (((Implication) e).leftOperand instanceof Exist && entityEquality(((Implication) e1).leftOperand,
                        ((Exist) ((Implication) e).leftOperand).operand)) {
                    exist = true;
                    if (checkInstances(((Exist) ((Implication) e).leftOperand).variable,
                            ((Implication) e).rightOperand, false)) {
                        if (!hypotheses.isEmpty()) {
                            if (!checkInstances(((Exist) ((Implication) e).leftOperand).variable,
                                    hypotheses.get(hypotheses.size() - 1), false)) {
                                existHyp = true;
                                return -1;
                            }
                        }
                        flag = i;
                        return flag;
                    }
                }
            }
        }
        return flag;
    }

    public ArrayList<String> replaceFromRules(Path path, ArrayList<String> names) {
        ArrayList<String> arrayList = new ArrayList<>();
        try (Scanner in = new Scanner(new File(path.toString()))) {
            while (in.hasNext()) {
                String str = in.next();
                str = str.replace("B", "K1");
                str = str.replace("C", "K2");
                str = str.replace("x", "K3");
                str = str.replace("A", "(" + (names.get(0)) + ")");
                if (names.size() > 1) {
                    str = str.replace("K1", "(" + names.get(1) + ")");
                }
                if (names.size() > 2) {
                    str = str.replace("K2", "(" + names.get(2) + ")");
                }
                if (names.size() > 3) {
                    str = str.replace("K3", names.get(3));
                }
                arrayList.add(str);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

}
