package structure;

/**
 * Created by Антон on 02.01.2017.
 */
public class Variable implements Entity {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
