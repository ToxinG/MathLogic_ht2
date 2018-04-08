public class Axioms {

    static String[] logicalAxioms = new String[] {
            ("a->(b->a)"),
            ("(a->b)->(a->(b->c))->(a->c)"),
            ("a->b->a&b"),
            ("a&b->a"),
            ("a&b->b"),
            ("a->a|b"),
            ("b->a|b"),
            ("(a->c)->(b->c)->(a|b->c)"),
            ("(a->b)->(a->!b)->!a"),
            ("!!a->a"),
    };

    static String[] arithmeticAxioms = new String[] {
            ("a=b->a'=b'"),
            ("(a=b)->(a=c)->(b=c)"),
            ("a'=b'->a=b"),
            ("!a'=0"),
            ("a+b'=(a+b)'"),
            ("a+0=a"),
            ("a*0=0"),
            ("a*b'=a*b+a"),
    };
}
