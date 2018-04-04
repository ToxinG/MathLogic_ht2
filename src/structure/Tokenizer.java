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
    public TokenType type;
    public String lastVar;
    public String lastPred;
    public String lastConst;

    public Tokenizer(String expr) {
        this.expr = expr;
        this.exprIndex = 0;
        lastVar = "";
        lastPred = "";
        lastConst = "";
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

        StringBuilder temp = new StringBuilder();

        if (isLowerCase(expr.charAt(exprIndex)) ||
                (isDigit(expr.charAt(exprIndex)) && type == TokenType.VAR)) {
            while (exprIndex < expr.length() && (isLowerCase(expr.charAt(exprIndex))) ||
                    isDigit(expr.charAt(exprIndex))) {
                temp.append(expr.charAt(exprIndex++));
                if (exprIndex >= expr.length())
                    break;
            }
            if (expr.charAt(exprIndex) == '(' && type != TokenType.ANY && type != TokenType.EXIST) {
                if (expr.charAt(exprIndex - 1) == '(' && (expr.charAt(exprIndex - 2) == '@' ||
                        expr.charAt(exprIndex - 2) == '?')) {
                    lastVar = temp.toString();
                }
                type = TokenType.FN;
                lastPred = temp.toString();
            } else {
                type = TokenType.VAR;
                lastVar = temp.toString();
            }
            return;
        }

        if (isUpperCase(expr.charAt(exprIndex)) || (isDigit(expr.charAt(exprIndex)) && type == TokenType.PRED)) {
            type = TokenType.PRED;
            while (exprIndex < expr.length() && isUpperCase(expr.charAt(exprIndex)) || isDigit(expr.charAt(exprIndex))) {
                lastPred = String.valueOf(expr.charAt(exprIndex++));
                if (exprIndex >= expr.length())
                    return;
            }
            return;
        }

        if (isDigit(expr.charAt(exprIndex))) {
            type = TokenType.CONST;
            while (exprIndex < expr.length() && isDigit(expr.charAt(exprIndex))) {
                lastConst = String.valueOf(expr.charAt(exprIndex++));
                if (exprIndex >= expr.length())
                    return;
            }
            return;
        }
    }
}
