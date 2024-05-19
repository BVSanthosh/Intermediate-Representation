class Let extends Expr {
    private String name;
    private Expr boundExpr;
    private Expr body;

    public Let(String name, Expr boundExpr, Expr body) {
        this.name = name;
        this.boundExpr = boundExpr;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public Expr getBoundExpr() {
        return boundExpr;
    }

    public Expr getBody() {
        return body;
    }
}