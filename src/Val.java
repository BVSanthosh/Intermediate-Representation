class Val extends Expr {
    private int value;

    public Val(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}