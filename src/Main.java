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
    private static ArrayList<Entity> hypList = new ArrayList<>();
    private static Entity[] axL = new Entity[11];
    private static Entity[] axA = new Entity[9];
    private static ArrayList<String> proofs = new ArrayList<>();
    private static ArrayList<Entity> proofList = new ArrayList<>();

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
        inference = inference.replace("\\s", "");


        String formula;
        if (inference.charAt(0) == '|') {
            formula = inference.substring(2);
        } else {
            String[] hypStrings = inference.split(",");
            String[] ending = hypStrings[hypStrings.length - 1].split("\\|\\-");
            formula = ending[1];
            hypStrings[hypStrings.length - 1] = ending[0];
            for (String hypString : hypStrings) hypList.add(parser.parseExpression(hypString));
        }
        target = parser.parseExpression(formula);

        String l;
        while (reader.ready()) {
            l = reader.readLine();
            if (l.equals(""))
                continue;
            l = l.replace(" ", "");
            proofs.add(l);
            proofList.add(parser.parseExpression(l));
        }
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
