import structure.Entity;

import java.io.*;
import java.util.ArrayList;

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
    private static int proofNumber = 1;

    public static void main(String[] args) throws IOException {

        long time = System.currentTimeMillis();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
            writer = new PrintWriter(new FileWriter(outputFile));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String inference = reader.readLine();
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
            if (hypList.get(0) != null)
                for (int i = 0; i < hypList.size(); i++)
                    if (checker.entityEquality(hypList.get(i), currExpr)) {
                        writer.write("(" + proofNumber++ + ") " + currStage + " (Предп. " + i + 1 + ")" + "\n");
                        checkedProofs.add(currExpr);
                        flag1 = true;
                        break;
                    }
            if (flag1)
                continue;

            int axNumber = checker.checkAxioms(currExpr, axL);
            if (axNumber > 0) {
                writer.write("(" + proofNumber++ + ") " + currStage + " (Сх. акс. " + axNumber + ")" + "\n");
                checkedProofs.add(currExpr);
                continue;
            }

            boolean flag2 = false;
            for (int j = 1; j < axA.length; j++)
                if (checker.entityEquality(axA[j], currExpr)) {
                    writer.write("(" + proofNumber++ + ") " + currStage + " (Aкс. " + (j) + ")" + "\n");
                    checkedProofs.add(currExpr);
                    flag2 = true;
                }
            if (flag2)
                continue;

            if (checker.checkAxiomA9(currExpr)) {
                writer.write("(" + proofNumber++ + ") " + currStage + " (Схема аксиом A9)" + "\n");
                checkedProofs.add(currExpr);
                continue;
            }

            Entity currExprCopy = currExpr.newInstance();

            checker.any = false;
            checker.anyHyp = false;
            int index = checker.checkAny(currExprCopy, checkedProofs, hypList);
            if (index > -1) {
                writer.write("(" + proofNumber++ + ") " + currStage + " (Введение квантора всеобщности)" + "\n");
                checkedProofs.add(currExprCopy);
                continue;
            }

            currExprCopy = currExpr.newInstance();

            checker.exist = false;
            checker.existHyp = false;
            index = checker.checkExist(currExprCopy, checkedProofs, hypList);
            if (index > -1) {
                writer.write("(" + proofNumber++ + ") " + currStage + " (Введение квантора существования)" + "\n");
                checkedProofs.add(currExprCopy);
                continue;
            }

            currExprCopy = currExpr.newInstance();

            int[] MP = checkModusPonens(currExprCopy);
        }
    }

    private static int[] checkModusPonens(Entity e) {

    }

    static void parseAxL() {
        axL[1] = parser.parseExpression("a->(b->a)");
        axL[2] = parser.parseExpression("(a->b)->(a->(b->c))->(a->c)");
        axL[3] = parser.parseExpression("a->b->a&b");
        axL[4] = parser.parseExpression("a&b->a");
        axL[5] = parser.parseExpression("a&b->b");
        axL[6] = parser.parseExpression("a->a|b");
        axL[7] = parser.parseExpression("b->a|b");
        axL[8] = parser.parseExpression("(a->c)->(b->c)->(a|b->c)");
        axL[9] = parser.parseExpression("(a->b)->(a->!b)->!a");
        axL[10] = parser.parseExpression("!!a->a");
    }

    static void parseAxA() {
        axA[1] = parser.parseExpression("a=b->a'=b'");
        axA[2] = parser.parseExpression("(a=b)->(a=c)->(b=c)");
        axA[3] = parser.parseExpression("a'=b'->a=b");
        axA[4] = parser.parseExpression("!a'=0");
        axA[5] = parser.parseExpression("a+b'=(a+b)'");
        axA[6] = parser.parseExpression("a+0=a");
        axA[7] = parser.parseExpression("a*0=0");
        axA[8] = parser.parseExpression("a*b'=a*b+a");
    }

}
