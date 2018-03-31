import structure.Entity;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Антон on 06.01.2017.
 */
public class Main {

    public static Entity target;
    public static ExpressionParser ep = new ExpressionParser();
    public static ArrayList<Entity> hypList = new ArrayList<>();
    public static Entity[] axL = new Entity[11];
    public static Entity[] axA = new Entity[9];
    public static ArrayList<String> proofs = new ArrayList<>();
    public static ArrayList<Entity> proofList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
        PrintWriter writer = new PrintWriter(new File("output.txt"));


        String inference = "";
        while (inference.equals(""))
            inference = reader.readLine();
        writer.println(inference);
        inference = inference.replace(" ", "");
        String formula;
        if (inference.charAt(0) == '|') {
            formula = inference.substring(2);
        } else {
            String[] hypStrings = inference.split(",");
            String[] ending = hypStrings[hypStrings.length - 1].split("\\|\\-");
            formula = ending[1];
            hypStrings[hypStrings.length - 1] = ending[0];
            for (String hypString : hypStrings) hypList.add(ep.parseExpression(hypString));
        }
        target = ep.parseExpression(formula);

        String l;
        while (reader.ready()) {
            l = reader.readLine();
            if (l.equals(""))
                continue;
            l = l.replace(" ", "");
            proofs.add(l);
            proofList.add(ep.parseExpression(l));
        }

    //PREDICATE MAY HAVE NO ARGUMENTS, FIX PARSER
//PREDICATE MAY HAVE NO ARGUMENTS, FIX PARSER
        //PREDICATE MAY HAVE NO ARGUMENTS, FIX PARSER
        //PREDICATE MAY HAVE NO ARGUMENTS, FIX PARSER
        //PREDICATE MAY HAVE NO ARGUMENTS, FIX PARSER
        //PREDICATE MAY HAVE NO ARGUMENTS, FIX PARSER

    }


    static void parseAxL() {
        axL[1] = ep.parseExpression("a->(b->a)");
        axL[2] = ep.parseExpression("(a->b)->(a->(b->c))->(a->c)");
        axL[3] = ep.parseExpression("a->b->a&b");
        axL[4] = ep.parseExpression("a&b->a");
        axL[5] = ep.parseExpression("a&b->b");
        axL[6] = ep.parseExpression("a->a|b");
        axL[7] = ep.parseExpression("b->a|b");
        axL[8] = ep.parseExpression("(a->c)->(b->c)->(a|b->c)");
        axL[9] = ep.parseExpression("(a->b)->(a->!b)->!a");
        axL[10] = ep.parseExpression("!!a->a");
    }

    static void parseAxA() {
        axA[1] = ep.parseExpression("a=b->a'=b'");
        axA[2] = ep.parseExpression("(a=b)->(a=c)->(b=c)");
        axA[3] = ep.parseExpression("a'=b'->a=b");
        axA[4] = ep.parseExpression("!a'=0");
        axA[5] = ep.parseExpression("a+b'=(a+b)'");
        axA[6] = ep.parseExpression("a+0=a");
        axA[7] = ep.parseExpression("a*0=0");
        axA[8] = ep.parseExpression("a*b'=a*b+a");
    }

}
