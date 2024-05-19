import java.util.List;

class Call extends Expr {
    private Expr function;
    private List<Expr> arguments;

    public Call(Expr function, List<Expr> arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    public Expr getFunction() {
        return function;
    }

    public List<Expr> getArguments() {
        return arguments;
    }
}