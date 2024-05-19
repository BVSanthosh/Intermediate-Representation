import java.util.List;

class Case extends Expr {
    private Expr expression;
    private List<CaseAlt> alternatives;

    public Case(Expr expression, List<CaseAlt> alternatives) {
        this.expression = expression;
        this.alternatives = alternatives;
    }

    public Expr getExpression() {
        return expression;
    }
    
    public List<CaseAlt> getAlternatives() {
        return alternatives;
    }
}