public class ANFVar extends Expr{
    private String name;

    public ANFVar(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
