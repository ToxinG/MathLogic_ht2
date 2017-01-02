/**
 * Created by Антон on 02.01.2017.
 */
public class LogicalUnaryEntity {
    LogicalUnaryEntity entity;
    enum EntityType {predicate, negation, parentheses, formula}
    String stringRepresentation = "";
    String unparsed = "";
}
