enum Op {
    PLUS, MINUS, TIMES, DIVIDE, EQ, LT, GT
}

class BinOp extends Expr {
    private Op operation;
    private Expr left;
    private Expr right;

    public BinOp(Op operation, Expr left, Expr right) {
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    public Op getOperation() {
        return this.operation;
    }

    public String getOperationStr() {
        String op = "";

        switch (this.operation) {
            case PLUS:
                op = "+";
                break;
            case MINUS:
                op = "-";
                break;
            case TIMES:
                op = "*";
                break;
            case DIVIDE:
                op = "/";
                break;
            case EQ:
                op = "==";
                break;
            case LT:
                op = "<";
                break;
            case GT:
                op = ">";
                break;
        }

        return op;
    }

    public Expr getLeft() {
        return this.left;
    }

    public Expr getRight() {
        return this.right;
    }
}