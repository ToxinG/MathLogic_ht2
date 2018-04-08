package structure;

/**
 * Created by Антон on 02.01.2017.
 */
public class Variable implements Entity {
    public String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        return (object instanceof Variable) && (((Variable) object).name.equals(name));
    }

    @Override
    public Entity newInstance() {
        return new Variable(name);
    }
}
