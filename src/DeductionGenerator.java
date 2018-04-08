import structure.Entity;
import structure.logical.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DeductionGenerator {
    public int pos;

    public ArrayList<Entity> checkedProofs = new ArrayList<>();
    public ArrayList<String> ind = new ArrayList<>();
    public ArrayList<Entity> hypList = new ArrayList<>();
    public ArrayList<String> head = new ArrayList<>();

    public ExpressionParser parser = new ExpressionParser();
    public ProofChecker checker = new ProofChecker();

    private static Entity[] axL = new Entity[11];
    private static Entity[] axA = new Entity[9];

    public ArrayList<String> deduct(String inference, ArrayList<String> proofList) throws IOException {

        checkedProofs = new ArrayList<>();
        ind = new ArrayList<>();

        ArrayList<String> out = new ArrayList<>();

        for(int i = 0; i < Axioms.logicalAxioms.length; i++)
            axL[i + 1] = parser.parseExpression(Axioms.logicalAxioms[i]);

        for(int i = 0; i < Axioms.arithmeticAxioms.length; i++)
            axA[i + 1] = parser.parseExpression(Axioms.arithmeticAxioms[i]);

        forSup(inference);
        String leftImpl = head.remove(head.size() - 1);

        for (String currStage : proofList) {

            Entity e = parser.parseExpression(currStage);
            Entity a = parser.parseExpression(leftImpl);
            Entity temp = parser.parseExpression(currStage);

            if (checker.entityEquality(e, a)) {
                checkedProofs.add(temp);
                ind.add(currStage);
                String g = "(" + leftImpl + ")->(" + leftImpl + ")->(" + leftImpl + ")";
                out.add(g);
                out.add("(" + g + ")->((" + leftImpl + ")->(((" + leftImpl + ")->(" +
                        leftImpl + "))->(" + leftImpl + ")))->((" + leftImpl + ")->(" + leftImpl + "))");
                out.add("((" + leftImpl + ")->(((" + leftImpl + ")->(" + leftImpl + "))->(" +
                        leftImpl + ")))->((" + leftImpl + ")->(" + leftImpl + "))");
                out.add("((" + leftImpl + ")->(((" + leftImpl + ")->(" +
                        leftImpl + "))->(" + leftImpl + ")))");
                out.add("(" + leftImpl + ")->(" + leftImpl + ")");
                continue;
            }
            boolean f = false;

            for (Entity hyp : hypList)
                if (hypList.get(0) != null && checker.entityEquality(hyp, e)) {
                    out.add(currStage);
                    checkedProofs.add(hyp);
                    ind.add(currStage);
                    String str2 = "(" + currStage + ")->(" + leftImpl + ")->(" + currStage + ")";
                    out.add(str2);
                    out.add("(" + leftImpl + ")->(" + currStage + ")");
                    f = true;
                    break;
                }
            if (f) {
                continue;
            }

            int a1 = checker.checkAxioms(e, axL);
            int a2 = 0;
            for (int j = 1; j < axA.length; j++)
                if (checker.entityEquality(axA[j], e)) {
                    a2 = j;
                    break;
                }
            if (a1 > 0 || a2 > 0 || checker.checkAxiomA9(e)) {
                checkedProofs.add(temp);
                ind.add(currStage);
                out.add(currStage);
                String str2 = "(" + currStage + ")->(" + leftImpl + ")->(" + currStage + ")";
                out.add(str2);
                out.add("(" + leftImpl + ")->(" + currStage + ")");
                continue;
            }

            if (checker.checkAny(e, checkedProofs, hypList) >= 0) {
                ArrayList<String> ar = new ArrayList<>();
                ar.add(leftImpl);
                ar.add(((Implication) e).leftOperand.toString());
                ar.add(((Any) ((Implication) e).rightOperand).operand.toString());
                ar.add(((Any) ((Implication) e).rightOperand).variable.toString());
                ArrayList<String> proofs = checker.replaceFromRules(Paths.get("rules/anyRules.rules"), ar);
                checkedProofs.add(temp);
                ind.add(currStage);
                out.addAll(proofs.stream().collect(Collectors.toList()));
                continue;
            }

            e = temp.newInstance();

            if (checker.checkExist(e, checkedProofs, hypList) >= 0) {
                ArrayList<String> ar = new ArrayList<>();
                ar.add(leftImpl);
                ar.add(((Exist) ((Implication) e).leftOperand).operand.toString());
                ar.add((((Implication) e).rightOperand).toString());
                ar.add(((Exist) ((Implication) e).leftOperand).variable.toString());
                ArrayList<String> proofs = checker.replaceFromRules(Paths.get("rules/existRules.rules"), ar);
                checkedProofs.add(temp);
                ind.add(currStage);
                out.addAll(proofs.stream().collect(Collectors.toList()));
                continue;
            }

            e = temp.newInstance();

            Integer[] MP = checkMP(e);
            if (MP[0] > -1) {
                checkedProofs.add(temp);
                ind.add(currStage);
                out.add("((" + leftImpl + ")->(" + ind.get(MP[0]) + "))->(((" + leftImpl + ")->((" +
                        ind.get(MP[0]) + ")->(" + currStage + ")))->((" + leftImpl + ")->(" + currStage + ")))");
                out.add("(((" + leftImpl + ")->((" + ind.get(MP[0]) + ")->(" + currStage + ")))->((" +
                        leftImpl + ")->(" + currStage + ")))");
                out.add("(" + leftImpl + ")->(" + currStage + ")");
                continue;
            }
            checkedProofs.add(temp);
            ind.add(currStage);
            out.add(currStage);

        }

        return out;
    }

    Integer[] checkMP(Entity e) {
        int to = -1;
        int from = -1;
        for (int i = checkedProofs.size() - 1; i >= 0; i--) {
            Entity e1 = checkedProofs.get(i);
            if (e1 instanceof Implication && checker.entityEquality(e, ((Implication) e1).rightOperand)) {
                for (int j = checkedProofs.size() - 1; j >= 0; j--) {
                    if (checker.entityEquality(((Implication) e1).leftOperand, checkedProofs.get(j))) {
                        from = i;
                        to = j;
                        break;
                    }
                }
                if (to > -1)
                    break;
            }
        }
        return new Integer[]{to, from};
    }

    private void forSup(String str) {

        int p = 0, num = 0, balance = 0;
        String cur = "";
        while (p < str.length()) {
            while (str.charAt(p) == ' ') p++;
            while (p < str.length() && (str.charAt(p) != ',' || balance != 0)
                    && p < str.length() && str.charAt(p) != '|') {
                if (str.charAt(p) != ' ') cur += str.charAt(p);
                if (str.charAt(p) == '(') balance++;
                if (str.charAt(p) == ')') balance--;
                p++;
            }

            num++;
            head.add(cur);
            hypList.add(parser.parseExpression(cur));

            cur = "";
            if (p < str.length() && str.charAt(p) == ',') p++;
            while (p < str.length() && str.charAt(p) == ' ') p++;
            if (p < str.length() && str.charAt(p) == '|') {
                p += 2;
                while (str.charAt(p) == ' ') p++;
                pos = p;
                p = str.length();
            }
        }
    }
}
