class Var extends Expr {
    private String name;

    public Var(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}