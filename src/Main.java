import structure.Entity;
import structure.logical.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Антон on 06.01.2017.
 */
public class Main {

    private static BufferedReader reader;
    private static PrintWriter writer;
    private final static File inputFile = new File("test.txt");
    private final static File outputFile = new File("output.txt");
    private static Entity target;
    private static ExpressionParser parser = new ExpressionParser();
    private static final ProofChecker checker = new ProofChecker();
    private static ArrayList<Entity> hypList = new ArrayList<>();
    private static Entity[] axL = new Entity[11];
    private static Entity[] axA = new Entity[9];
    private static ArrayList<String> proofs = new ArrayList<>();
    private static ArrayList<Entity> checkedProofs = new ArrayList<>();
    private static HashMap<String, Entity> exprMap;
    private static String inference;
    private static int proofNumber = 1;

    public static void main(String[] args) throws IOException {

        long time = System.currentTimeMillis();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
            writer = new PrintWriter(new FileWriter(outputFile));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < Axioms.logicalAxioms.length; i++)
            axL[i + 1] = parser.parseExpression(Axioms.logicalAxioms[i]);

        for(int i = 0; i < Axioms.arithmeticAxioms.length; i++)
            axA[i + 1] = parser.parseExpression(Axioms.arithmeticAxioms[i]);

        inference = reader.readLine();
        writer.println(inference);
        inference = inference.replaceAll("\\s", "");


        String formula;
        if (inference.charAt(0) == '|') {
            formula = inference.substring(2);
        } else {
            String[] hypStrings = inference.split(",");
            String[] ending = hypStrings[hypStrings.length - 1].split("\\|-");
            formula = ending[1];
            hypStrings[hypStrings.length - 1] = ending[0];
            for (String hypString : hypStrings) hypList.add(parser.parseExpression(hypString));
        }
        target = parser.parseExpression(formula);

        String currStage;
        while (reader.ready()) {
            currStage = reader.readLine();
            if (currStage.equals(""))
                continue;
            currStage = currStage.replaceAll("\\s", "");
            proofs.add(currStage);

            Entity currExpr;
            currExpr = parser.parseExpression(currStage);
            checker.changing = false;
            boolean flag1 = false;
            if (!hypList.isEmpty())
                for (int i = 0; i < hypList.size(); i++)
                    if (checker.entityEquality(hypList.get(i), currExpr)) {
                        writer.write("(" + proofNumber++ + ") " + currStage + " (Предп. " + (i + 1) + ")" + "\n");
                        checkedProofs.add(currExpr);
                        flag1 = true;
                        break;
                    }
            if (flag1)
                continue;

            Entity currExprCopy = currExpr.newInstance();


            int axNumber = checker.checkAxioms(currExprCopy, axL);
            if (axNumber > 0) {
                writer.write("(" + proofNumber++ + ") " + currStage + " (Сх. акс. " + axNumber + ")" + "\n");
                checkedProofs.add(currExpr);
                continue;
            }

            currExprCopy = currExpr.newInstance();

            boolean flag2 = false;
            for (int j = 1; j < axA.length; j++) {
                exprMap = new HashMap<>();
                if (checker.entityConformity(axA[j], currExprCopy, exprMap)) {
                    writer.write("(" + proofNumber++ + ") " + currStage + " (Aкс. " + (j) + ")" + "\n");
                    checkedProofs.add(currExpr);
                    flag2 = true;
                    break;
                }
            }
            if (flag2)
                continue;

            currExprCopy = currExpr.newInstance();

            if (checker.checkAxiomA9(currExprCopy)) {
                writer.write("(" + proofNumber++ + ") " + currStage + " (Схема аксиом A9)" + "\n");
                checkedProofs.add(currExpr);
                continue;
            }

            currExprCopy = currExpr.newInstance();

            checker.any = false;
            checker.anyHyp = false;
            int index = checker.checkAny(currExprCopy, checkedProofs, hypList);
            if (index > -1) {
                writer.write("(" + proofNumber++ + ") " + currStage + " (Введение квантора всеобщности)" + "\n");
                checkedProofs.add(currExpr);
                continue;
            }

            currExprCopy = currExpr.newInstance();

            checker.exist = false;
            checker.existHyp = false;
            index = checker.checkExist(currExprCopy, checkedProofs, hypList);
            if (index > -1) {
                writer.write("(" + proofNumber++ + ") " + currStage + " (Введение квантора существования)" + "\n");
                checkedProofs.add(currExpr);
                continue;
            }

            currExprCopy = currExpr.newInstance();

            int[] MP = checkModusPonens(currExprCopy);
            if (MP[0] > -1) {
                writer.write("(" + proofNumber++ + ") " + currStage +
                        " (M.P. " + ++MP[0] + ", " + ++MP[1] + ")" + "\n");
                checkedProofs.add(currExpr);
                continue;
            }



            writer.write("(" + proofNumber + ") " + currStage +
                    " Вывод некорректен, начиная с формулы №" + proofNumber++);

            reportMistake(currExprCopy, currStage);

            System.out.println("End of proof (HW2).\n[Time = " +
                    (System.currentTimeMillis() - time) + "ms]");

            writer.close();
            reader.close();
            return;
        }

        if (hypList.isEmpty()) {
            printOriginalProof();
        } else {
            printDeduction();
        }
        System.out.println("End of proof (HW2).\n[Time = " +
                (System.currentTimeMillis() - time) + "ms]");

        writer.close();
        reader.close();
    }

    private static int[] checkModusPonens(Entity e) {
        int to = -1;
        int from = -1;
        for (int i = checkedProofs.size() - 1; i >= 0; i--) {
            Entity e1 = checkedProofs.get(i);
            if (e1 instanceof Implication && checker.entityEquality(e, ((Implication) e1).rightOperand)) {
                for (int j = checkedProofs.size() - 1; j >= 0; j--) {
                    exprMap = new HashMap<>();
                    if (checker.entityConformity(((Implication) e1).leftOperand, checkedProofs.get(j), exprMap)) {
                        from = i;
                        to = j;
                        break;
                    }
                }
                if (to > -1)
                    break;
            }
        }
        return new int[]{to, from};
    }

    private static void reportMistake(Entity e, String string) {
        if (checker.any && checker.anyHyp) {
            writer.write(": используется правило с квантором по переменной " +
                    ((Any) ((Implication) e).rightOperand).variable.name + " входящей свободно в допущение " +
                    hypList.get(hypList.size() - 1));
        } else if (checker.any) {
            writer.write(": Переменная " + ((Any) ((Implication) e).rightOperand).variable.name +
                    " входит свободно в формулу " + (((Implication) e).leftOperand) + "\n");
        }

        if (checker.exist && checker.existHyp) {
            writer.write(": используется правило с квантором по переменной " +
                    ((Exist) ((Implication) e).leftOperand).variable.name + " входящей свободно в допущение " +
                    hypList.get(hypList.size() - 1));
        } else if (checker.exist) {
            writer.write(": Переменная " + ((Exist) ((Implication) e).leftOperand).variable.name +
                    " входит свободно в формулу " + (((Implication) e).rightOperand) + "\n");
        }

        if (checker.changing) {
            Entity e1 = null, e2 = null;
            if (e instanceof Implication && ((Implication) e).leftOperand instanceof Any) {
                checker.entityConformity(((Implication) e).rightOperand,
                        ((Any) ((Implication) e).leftOperand).operand, exprMap);
                e1 = exprMap.get((exprMap.keySet().toArray()[0]));
                e2 = ((Any) ((Implication) e).leftOperand).variable;
            } else if (e instanceof Implication && ((Implication) e).leftOperand instanceof Exist) {
                checker.entityConformity(((Implication) e).leftOperand,
                        ((Exist) ((Implication) e).rightOperand).operand, exprMap);
                e1 = exprMap.get(((Exist) ((Implication) e).rightOperand).variable.name);
                e2 = ((Exist) ((Implication) e).rightOperand).variable;
            }

            writer.write(": Терм " + e1 + " не свободен для подстановки в формулу " +
                    string + " вместо переменной " + e2 + "\n");
        }
    }

    private static void printOriginalProof() throws IOException {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
        try {
            writer = new PrintWriter(new FileWriter(outputFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String tempLine = reader.readLine();
        while (tempLine != null) {
            writer.println(tempLine);
            tempLine = reader.readLine();
        }
    }

    private static void printDeduction() throws IOException {
        if (writer != null) {
            writer.close();
        }
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
        try {
            writer = new PrintWriter(new FileWriter(outputFile));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        DeductionGenerator dg = new DeductionGenerator();
        try {
            ArrayList<String> str = dg.deduct(inference, proofs);
            str.stream().forEach((s) -> { writer.write(s + "\n"); });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
