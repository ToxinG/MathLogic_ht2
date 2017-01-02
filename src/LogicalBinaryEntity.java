/**
 * Created by Антон on 02.01.2017.
 */
public class LogicalBinaryEntity {
    LogicalBinaryEntity left, right;
    enum EntityType {implication, conjunction, disjunction, none}
    EntityType type = EntityType.none;
    String stringRepresentation = "";
    String unparsed = "";
}
