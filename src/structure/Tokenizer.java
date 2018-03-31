package structure;

import static java.lang.Character.*;

/**
 * Created by Антон on 03.01.2017.
 */
public class Tokenizer {

    public enum TokenType {
        EXPR, IMPL, AND, OR, PRED, EQUAL, NEG, ANY, EXIST,
        OPAR, CPAR, COMMA, END,
        ADD, MUL, VAR, CONST, INC, FN
    }

    private String expr;
    private int exprIndex;
    public String currentToken;
    public TokenType type;

    public Tokenizer(String expr) {
        this.expr = expr;
        this.exprIndex = 0;
    }

    public void getToken() {

        while (exprIndex < expr.length() && isWhitespace(expr.charAt(exprIndex)))
            exprIndex++;

        if (exprIndex >= expr.length()) {
            type = TokenType.END;
            return;
        }

        switch (expr.charAt(exprIndex)) {
            case '-': {
                type = TokenType.IMPL;
                exprIndex += 2;
                return;
            }
            case '&': {
                type = TokenType.AND;
                exprIndex++;
                return;
            }
            case '|': {
                type = TokenType.OR;
                exprIndex++;
                return;
            }
            case '=': {
                type = TokenType.EQUAL;
                exprIndex++;
                return;
            }
            case '!': {
                type = TokenType.NEG;
                exprIndex++;
                return;
            }
            case '@': {
                type = TokenType.ANY;
                exprIndex++;
                return;
            }
            case '?': {
                type = TokenType.EXIST;
                exprIndex++;
                return;
            }
            case '(': {
                type = TokenType.OPAR;
                exprIndex++;
                return;
            }
            case ')': {
                type = TokenType.CPAR;
                exprIndex++;
                return;
            }
            case ',': {
                type = TokenType.COMMA;
                exprIndex++;
                return;
            }
            case '+': {
                type = TokenType.ADD;
                exprIndex++;
                return;
            }
            case '*': {
                type = TokenType.MUL;
                exprIndex++;
                return;
            }
            case '\'': {
                type = TokenType.INC;
                exprIndex++;
                return;
            }
        }

        String name = "";

        if (isUpperCase(expr.charAt(exprIndex))) {
            name += expr.charAt(exprIndex);
            exprIndex++;
            while (isDigit(expr.charAt(exprIndex))) {
                name += expr.charAt(exprIndex);
                exprIndex++;
            }
            type = TokenType.PRED;
            currentToken = name;
            return;
        }

        if (isLowerCase(expr.charAt(exprIndex))) {
            name += expr.charAt(exprIndex);
            exprIndex++;
            while (isDigit(expr.charAt(exprIndex))) {
                name += expr.charAt(exprIndex);
                exprIndex++;
            }
            if (expr.charAt(exprIndex) == '(') {
                type = TokenType.FN;
            } else {
                type = TokenType.VAR;
            }
            currentToken = name;
            return;
        }

        if (isDigit(expr.charAt(exprIndex))) {
            while (isDigit(expr.charAt(exprIndex))) {
                name += expr.charAt(exprIndex);
                exprIndex++;
            }
            currentToken = name;
            type = TokenType.CONST;
            return;
        }
    }
}
