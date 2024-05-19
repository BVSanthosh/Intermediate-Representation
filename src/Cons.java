import java.util.List;

class Cons extends Expr {
    private String name;
    private List<Expr> arguments;

    public Cons(String name, List<Expr> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public List<Expr> getArguments() {
        return arguments;
    }
}
