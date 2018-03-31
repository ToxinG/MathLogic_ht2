import structure.*;
import structure.arithmetic.*;
import structure.logical.*;
import structure.Tokenizer.TokenType;

import java.util.ArrayList;

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

    private Entity parseUnary() {
        Entity res;
        if (tz.type == TokenType.NEG) {
            tz.getToken();
            return new Negation(parseUnary());
        }
        if (tz.type == TokenType.EXIST || tz.type == TokenType.ANY)
            return parseFormula();
        if (tz.type == TokenType.OPAR) {
            while (tz.type == TokenType.OPAR)
                tz.getToken();
            res = parseImplication();
            while (tz.type == TokenType.CPAR)
                tz.getToken();
            return res;
        }
        return parsePredicate();
    }

    private Entity parsePredicate() {
        Predicate res;
        res = new Predicate();
        ArrayList<ArithmeticEntity> operands = new ArrayList<>();
        if (tz.type == TokenType.PRED) {
            res.name = tz.currentToken;
            tz.getToken();
            tz.getToken();
            while (tz.type != TokenType.CPAR) {
                operands.add(parseSum());
                tz.getToken();
                if (tz.type == TokenType.CPAR)
                    break;
                tz.getToken();
            }
        } else {
            operands.add(parseSum());
            res.type = "=";
            tz.getToken();
            tz.getToken();
            operands.add(parseSum());
        }
        res.operands = operands;
        return res;
    }

    private Entity parseFormula() {
        String type = tz.currentToken;
        tz.getToken();
        Variable v = new Variable(tz.currentToken);
        tz.getToken();
        Entity u = parseUnary();
        return new Formula(type, v, u);
    }

    private ArithmeticEntity parseSum() {
        ArithmeticEntity res = parseMult();
        while (tz.type == TokenType.ADD) {
            tz.getToken();
            res = new Sum(res, parseMult());
        }
        return res;
    }

    private ArithmeticEntity parseMult() {
        ArithmeticEntity res = parseItem();
        while (tz.type == TokenType.MUL || tz.type == TokenType.INC) {
            if (tz.type == TokenType.MUL) {
                tz.getToken();
                res = new Multiplication(res, parseItem());
            } else {
                tz.getToken();
                res = new Increment(res);
            }
        }
        return res;
    }

    private ArithmeticEntity parseItem() {
        ArithmeticEntity res;
        if (tz.type == TokenType.OPAR) {
            while (tz.type == TokenType.OPAR)
                tz.getToken();
            res = parseItem();
            while (tz.type == TokenType.CPAR)
                tz.getToken();
            return res;
        }
        if (tz.type == TokenType.VAR) {
            return new Variable(tz.currentToken);
        }
        if (tz.type == TokenType.CONST) {
            return new Constant();
        }
        return parseFunction();
    }

    private ArithmeticEntity parseFunction() {
        Function res = new Function();
        res.name = tz.currentToken;
        tz.getToken();
        tz.getToken();
        ArrayList<ArithmeticEntity> operands = new ArrayList<>();
        while (tz.type != TokenType.CPAR) {
            operands.add(parseSum());
            tz.getToken();
            if (tz.type == TokenType.CPAR)
                break;
            tz.getToken();
        }
        res.operands = operands;
        return res;
    }
}
