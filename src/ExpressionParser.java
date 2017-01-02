import structure.*;

import static java.lang.Character.*;

/**
 * Created by Антон on 02.01.2017.
 */

public class ExpressionParser {

    public Entity parseExpression(String s) {
        Entity res = parseDisjunction(s);

        return res;
    }

    private Entity parseDisjunction(String s) {
        Entity res = parseConjunction(s);

        return res;
    }

    private Entity parseConjunction(String s) {
        Entity res = parseUnary(s);

        return res;
    }

    private Entity parseUnary(String s) {
        Entity res;
        if (isLetter(s.charAt(0)) || isDigit(s.charAt(0))) {
            res = parsePredicate(s);
        } else if (s.charAt(0) == '!') {
            res = parseNegation(s);
        } else if (s.charAt(0) == '(') {
            res = parseExpression(s); // FIND CLOSING BRACKET
        } else if (s.charAt(0) == '@' || s.charAt(0) == '?') {
            res = parseFormula(s);
        }
    }
}
