import java.util.List;

public class CaseAlt extends Expr{
    private String name;
    private List<String> binders;
    private Expr expression;

    public CaseAlt(String name, List<String> binders, Expr expression) {
        this.name = name;
        this.binders = binders;
        this.expression = expression;
    }

    public String getName() {
        return name;
    }

    public List<String> getBinders() {
        return binders;
    }

    public Expr getExpression() {
        return expression;
    }
}