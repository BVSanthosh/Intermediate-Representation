import java.util.List;

public class Program {
    private List<Function> functions;
    private Expr expression;

    public Program(List<Function> functions, Expr expression) {
        this.functions = functions;
        this.expression = expression;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public Expr getExpression() {
        return expression;
    }
}