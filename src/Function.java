import java.util.List;

public class Function {
    private String name;
    private List<String> arguments;
    private Expr body;

    public Function(String name, List<String> arguments, Expr body) {
        this.name = name;
        this.arguments = arguments;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public Expr getBody() {
        return body;
    }
}