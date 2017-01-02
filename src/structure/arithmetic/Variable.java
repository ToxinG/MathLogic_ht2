package structure.arithmetic;

/**
 * Created by Антон on 02.01.2017.
 */
public class Variable implements ArithmeticEntity {
    private String name;

    Variable(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
