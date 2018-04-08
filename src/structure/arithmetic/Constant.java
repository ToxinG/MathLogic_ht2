package structure.arithmetic;

import structure.Entity;

/**
 * Created by Антон on 02.01.2017.
 */
public class Constant implements Entity {

    public String name;

    public Constant(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Entity newInstance() {
        return new Constant(name);
    }
}
