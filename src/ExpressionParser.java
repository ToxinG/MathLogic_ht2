import structure.*;
import structure.arithmetic.*;
import structure.logical.*;
import structure.Tokenizer.TokenType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Created by Антон on 02.01.2017.
 */

public class ExpressionParser {

    Tokenizer tz;

    public Entity parseExpression(String s) {
        tz = new Tokenizer(s);
        tz.getToken();
        return parseImplication();
    }

    private Entity parseImplication() {
        Entity res = parseDisjunction();
        if (tz.type == TokenType.IMPL) {
            tz.getToken();
            res = new Implication(res, parseImplication());
        }
        return res;
    }

    private Entity parseDisjunction() {
        Entity res = parseConjunction();
        while (tz.type == TokenType.OR) {
            tz.getToken();
            res = new Disjunction(res, parseConjunction());
        }
        return res;
    }

    private Entity parseConjunction() {
        Entity res = parseUnary();
        while (tz.type == TokenType.AND) {
            tz.getToken();
            res = new Conjunction(res, parseUnary());
        }

        return res;
    }

//transformed tail recursion generated by multiple quantifiers or negations to a cycle
private Entity parseUnary() {
    Entity res;
    ArrayList<TokenType> tokens = new ArrayList<>();
    Stack<Variable> names = new Stack<>();
    while (tz.type == TokenType.NEG || tz.type == TokenType.ANY || tz.type == TokenType.EXIST)
        if (tz.type == TokenType.NEG) {
            tokens.add(tz.type);
            tz.getToken();
        } else {
            tokens.add(tz.type);
            tz.getToken();
            while (tz.type == TokenType.OPAR)
                tz.getToken();
            names.push(new Variable(tz.lastVar));
            tz.getToken();
            while (tz.type == TokenType.CPAR)
                tz.getToken();
        }
    res =  parsePredicate();
    Collections.reverse(tokens);
    for (TokenType t : tokens) {
        if (t == TokenType.NEG)
            res = new Negation(res);
        if (t == TokenType.ANY)
            res = new Any(names.pop(), res);
        if (t == TokenType.EXIST)
            res = new Exist(names.pop(), res);
    }
    return res;
}

    private Entity parsePredicate() {
        Entity res = parseSum();
        if (res == null) {
            while (tz.type == TokenType.PRED) {
                tz.getToken();
                String name = tz.lastPred;
                ArrayList<Entity> vars = new ArrayList<>();
                if (tz.type == TokenType.OPAR) {
                    do {
                        tz.getToken();
                        vars.add(parseSum());
                    } while (tz.type != TokenType.CPAR);
                    tz.getToken();
                }
                res = new Predicate(name, vars);
            }
        }
        return res;
    }

    private Entity parseSum() {
        Entity res = parseMult();
        while (tz.type == TokenType.EQUAL) {
            tz.getToken();
            res = new Equality(res, parseMult());
        }
        return res;
    }

    private Entity parseMult() {
        Entity res = parseIncrement();
        while (tz.type == TokenType.MUL || tz.type == TokenType.ADD) {
            if (tz.type == TokenType.MUL) {
                tz.getToken();
                res = new Multiplication(res, parseIncrement());
            } else {
                tz.getToken();
                res = new Sum(res, parseIncrement());
            }
        }
        return res;
    }

    private Entity parseIncrement() {
        Entity res = parseFunction();
        while (tz.type == TokenType.INC) {
            tz.getToken();
            res = new Increment(res);
        }
        return res;
    }

    private Entity parseFunction() {
        Entity res = null;
        switch (tz.type) {
            case OPAR: {
                tz.getToken();
                res = parseImplication();
                tz.getToken();
                break;
            }
            case VAR: {
                res = new Variable(tz.lastVar);
                tz.getToken();
                break;
            }
            case CONST: {
                res = new Constant(tz.lastConst);
                tz.getToken();
                break;
            }
        }
        if (res == null) {
            while (tz.type == TokenType.FN) {
                tz.getToken();
                String name = tz.lastFunc;
                ArrayList<Entity> vars = new ArrayList<>();
                if (tz.type == TokenType.OPAR) {
                    do {
                        tz.getToken();
                        vars.add(parseSum());
                    } while (tz.type != TokenType.CPAR);
                    tz.getToken();
                }
                res = new Function(name, vars);
            }
        }
        return res;
    }
}
