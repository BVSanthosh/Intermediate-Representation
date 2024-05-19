import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compiler {
    public static void main(String[] args) {

        /*
         * List of all the example ASTs used for the testing of the compiler.
         */

        /* identity(x) = x */
        Var var32 = new Var("x");
        Function identity_def = new Function("identity", Arrays.asList("x"), var32);

        /* double(val) = val * 2 */
        Var var = new Var("val");
        Val val = new Val(2);
        BinOp binop = new BinOp(Op.TIMES, var, val);
        Function double_def = new Function("double", Arrays.asList("val"), binop);

        /* complex_expr1(x, y) = (x * x) + (y - 10) */
        Var var1 = new Var("x");
        Var var27 = new Var("y");
        Val val1 = new Val(10);
        BinOp binop1 = new BinOp(Op.MINUS, var27, val1);
        BinOp binop5 = new BinOp(Op.TIMES, var1, var1);
        BinOp binop6 = new BinOp(Op.PLUS, binop5, binop1);
        Function complex_expr1_def = new Function("complex_expr1", Arrays.asList("x", "y"), binop6);

        /* complex_expr2(w, x, y, z) = ((w - x) * y) + z*/
        Var var28 = new Var("w");
        Var var29 = new Var("x");
        Var var30 = new Var("y");
        Var var31 = new Var("z");
        BinOp binop7 = new BinOp(Op.MINUS, var28, var29);
        BinOp binop8 = new BinOp(Op.TIMES, binop7, var30);
        BinOp binop9 = new BinOp(Op.PLUS, binop8, var31);
        Function complex_expr2_def = new Function("complex_expr2", Arrays.asList("w", "x", "y", "z"), binop9);

        /* testlist() = Cons 1 (Cons 2 Nil) */
        Val val6 = new Val(1);
        Val val7 = new Val(2);
        Var var17 = new Var("testlist");
        Cons con = new Cons("Nil", Arrays.asList());
        Cons con2 = new Cons("Cons", Arrays.asList(val7, con));
        Cons con3 = new Cons("Cons", Arrays.asList(val6, con2));
        Function testlist_def = new Function("testlist", Arrays.asList(), con3);

        /* testpair() = MkPair(15, 20) */
        Val val40 = new Val(15);
        Val val41 = new Val(20);
        Cons con15 = new Cons("MkPair", Arrays.asList(val40, val41));
        Function testpair_def = new Function("testpair", Arrays.asList(), con15);

        /* testnil() = Nil() */
        Cons con16 = new Cons("Nil", Arrays.asList());
        Function testnil_def = new Function("testnil", Arrays.asList(), con16);

        /* factorial(x) = if x == 0
                then 1
                else x * factorial(x-1)*/
        Var var3 = new Var("x");
        Var var4 = new Var("factorial");
        Val val3 = new Val(0);
        Val val4 = new Val(1);
        BinOp binop2 = new BinOp(Op.EQ, var3, val3);
        BinOp binop4 = new BinOp(Op.MINUS, var3, val4);
        Call call2 = new Call(var4, Arrays.asList(binop4));
        BinOp binop3 = new BinOp(Op.TIMES, var3, call2);
        If ifcon = new If(binop2, val4, binop3);
        Function factor_def = new Function("factorial", Arrays.asList("x"), ifcon);

        /* complex_if1(x, y) = if x < 5
                then y + 1
                else 
                    if x == 6
                        then y + 2
                        else y + 3 */
        Var var36 = new Var("x");
        Var var37 = new Var("y");
        Val val19 = new Val(5);
        Val val20 = new Val(6);
        Val val21 = new Val(1);
        Val val22 = new Val(2);
        Val val23 = new Val(3);
        BinOp binop10 = new BinOp(Op.LT, var36, val19);
        BinOp binop11 = new BinOp(Op.EQ, var36, val20);
        BinOp binop12 = new BinOp(Op.PLUS, var37, val21);
        BinOp binop13 = new BinOp(Op.PLUS, var37, val22);
        BinOp binop14 = new BinOp(Op.PLUS, var37, val23);
        If ifcon2 = new If(binop11, binop13, binop14);
        If ifcon3 = new If(binop10, binop12, ifcon2);
        Function complex_if1_def = new Function("complex_if1", Arrays.asList("x", "y"), ifcon3);

        /* complex_if2(x, y, z) = if x == y 
            then
                if y == z
                    then (x * 10) + 1
                    else y + z + 2
            else 
                if z < 10
                    then y - 3
                    else z - 4*/
        Var var39 = new Var("x");
        Var var40 = new Var("y");
        Var var41 = new Var("z");
        Val val26 = new Val(10);
        Val val27 = new Val(1);
        Val val28 = new Val(2);
        Val val29 = new Val(3);
        Val val30 = new Val(4);
        BinOp binop15 = new BinOp(Op.EQ, var39, var40);
        BinOp binop16 = new BinOp(Op.EQ, var40, var41);
        BinOp binop17 = new BinOp(Op.TIMES, var39, val26);
        BinOp binop18 = new BinOp(Op.PLUS, binop17, val27);
        BinOp binop19 = new BinOp(Op.PLUS, var40, var41);
        BinOp binop20 = new BinOp(Op.PLUS, binop19, val28);
        BinOp binop21 = new BinOp(Op.LT, var41, val26);
        BinOp binop22 = new BinOp(Op.MINUS, var40, val29);
        BinOp binop23 = new BinOp(Op.MINUS, var41, val30);
        If ifcon4 = new If(binop16, binop18, binop20);
        If ifcon6 = new If(binop21, binop22, binop23);
        If ifcon5 = new If(binop15, ifcon4, ifcon6);
        Function complex_if2_def = new Function("complex_if2", Arrays.asList("x", "y", "z"), ifcon5);

        /* complex_if3(val, p) = if val > 4
              then case p of
                    Nil() -> 0
                    Cons(x, xs) -> y + y
                else case p of
                    Nil() -> 0
                    Cons(x, xs) -> x + x + x */
        Var var49 = new Var("val");
        Var var50 = new Var("p");
        Var var51 = new Var("x");
        Val val42 = new Val(4);
        Val val43 = new Val(0);
        Val val44 = new Val(3);
        Val val45 = new Val(2);
        Val val46 = new Val(1);
        BinOp binop24 = new BinOp(Op.GT, var49, val42);
        BinOp binop25 = new BinOp(Op.PLUS, var51, var51);
        BinOp binop26 = new BinOp(Op.PLUS, binop25, var51);
        Cons cons4 = new Cons("Nil", Arrays.asList());
        Cons cons5 = new Cons("Cons", Arrays.asList(val44, cons4));
        Cons cons6 = new Cons("Cons", Arrays.asList(val45, cons5));
        Cons cons7 = new Cons("Cons", Arrays.asList(val46, cons6));
        CaseAlt casealt9 = new CaseAlt("Nil", Arrays.asList(), val43);
        CaseAlt casealt10 = new CaseAlt("Cons", Arrays.asList("x", "xs"), binop25);
        Case casecon6 = new Case(var50, Arrays.asList(casealt9, casealt10));
        CaseAlt casealt11 = new CaseAlt("Nil", Arrays.asList(), val43);
        CaseAlt casealt12 = new CaseAlt("Cons", Arrays.asList("x", "xs"), binop26);
        Case casecon7 = new Case(var50, Arrays.asList(casealt11, casealt12));
        If ifcon7 = new If(binop24, casecon6, casecon7);
        Function complex_if3_def = new Function("complex_if3", Arrays.asList("val", "p"), ifcon7);

        /* complex_if4(x, y, p) = if x == y 
            then case p of
                Nil() -> 0
                Cons(z, zs) -> Cons(x + z, zs)
            else 
                if y < 10
                    then y + 2 / x
                    else y - 4*/
        Var var54 = new Var("x");
        Var var55 = new Var("y");
        Var var56 = new Var("p");
        Var var57 = new Var("z");
        Val val48 = new Val(10);
        Val val49 = new Val(2);
        Val val50 = new Val(4);
        Val val51 = new Val(0);
        Val val52 = new Val(5);
        Val val53 = new Val(7);
        BinOp binop27 = new BinOp(Op.EQ, var54, var55);
        BinOp binop28 = new BinOp(Op.LT, var55, val48);
        BinOp binop29 = new BinOp(Op.PLUS, var55, val49);
        BinOp binop30 = new BinOp(Op.DIVIDE, binop29, var54);
        BinOp binop31 = new BinOp(Op.MINUS, var55, val50);
        BinOp binop32 = new BinOp(Op.PLUS, var54, var57);
        Cons cons8 = new Cons("Nil", Arrays.asList());
        Cons cons9 = new Cons("Cons", Arrays.asList(val53, cons8));
        Cons cons10 = new Cons("Cons", Arrays.asList(val52, cons9));
        Cons cons11 = new Cons("Cons", Arrays.asList(binop32, cons9));
        CaseAlt casealt13 = new CaseAlt("Nil", Arrays.asList(), val51);
        CaseAlt casealt14 = new CaseAlt("Cons", Arrays.asList("z", "zs"), cons11);
        Case casecon8 = new Case(var56, Arrays.asList(casealt13, casealt14));
        If ifcon8 = new If(binop28, binop30, binop31);
        If ifcon9 = new If(binop27, casecon8, ifcon8);
        Function complex_if4_def = new Function("complex_if4", Arrays.asList("x", "y", "p"), ifcon9);

        /* snd(p) = case p of
              MkPair(x,y) -> y */
        Var var9 = new Var("p");
        Var var12 = new Var("y");
        CaseAlt casealt2 = new CaseAlt("MkPair", Arrays.asList("x", "y"), var12);
        Case casecon2 = new Case(var9, Arrays.asList(casealt2));
        Function snd_def = new Function("snd", Arrays.asList("p"), casecon2);

        /* sum(xs) = case xs of
               Nil -> 0
               Cons(y, ys) -> y + sum(ys) */
        Var var13 = new Var("xs");
        Var var14 = new Var("sum");
        Var var15 = new Var("y");
        Var var16 = new Var("ys");
        Val val5 = new Val(0);
        Call call3 = new Call(var14, Arrays.asList(var16));
        BinOp binOp5 = new BinOp(Op.PLUS, var15, call3);
        CaseAlt casealt3 = new CaseAlt("Nil", Arrays.asList(), val5);
        CaseAlt casealt4 = new CaseAlt("Cons", Arrays.asList("y", "ys"), binOp5);
        Case casecon3 = new Case(var13, Arrays.asList(casealt3, casealt4));
        Function sum_def = new Function("sum", Arrays.asList("xs"), casecon3);

        /* complex_case1(y, xs) = case xs of
                Nil -> 0
                Cons(x, xs) -> if x < y
                    then x + complex_case1(y, xs)
                    else y + x + complex_case1(y, xs) */
        Var var64 = new Var("y");
        Var var60 = new Var("xs");
        Var var61 = new Var("x");
        Var var62 = new Var("xs");
        Var var63 = new Var("complex_case1");
        Val val54 = new Val(0);
        Val val55 = new Val(4);
        Val val56 = new Val(5);
        Val val57 = new Val(6);
        Cons cons12 = new Cons("Nil", Arrays.asList());
        Cons cons13 = new Cons("Cons", Arrays.asList(val57, cons12));
        Cons cons14 = new Cons("Cons", Arrays.asList(val56, cons13));
        Cons cons15 = new Cons("Cons", Arrays.asList(val55, cons14));
        Call call22 = new Call(var63, Arrays.asList(var64, var62));
        BinOp binOp10 = new BinOp(Op.LT, var61, var64);
        BinOp binOp11 = new BinOp(Op.PLUS, var61, call22);
        BinOp binOp12 = new BinOp(Op.PLUS, var61, call22);
        BinOp binOp13 = new BinOp(Op.PLUS, var64, binOp12);
        If ifcon10 = new If(binOp10, binOp11, binOp13);
        CaseAlt casealt15 = new CaseAlt("Nil", Arrays.asList(), val54);
        CaseAlt casealt16 = new CaseAlt("Cons", Arrays.asList("x", "xs"), ifcon10);
        Case casecon9 = new Case(var60, Arrays.asList(casealt15, casealt16));
        Function complex_case1_def = new Function("complex_case1", Arrays.asList("y", "xs"), casecon9);

        /* complex_case2(x, y, z) = case y of
                Nil() -> 0
                MkPair(a, b) -> if x == a
                    then case z of
                        Nil() -> 0
                        MkPair(c, d) -> if x == c
                            then MkPair(a + c, b + d)
                            else MkPair(a * x, b * x)
                    else 0 */
        Var var65 = new Var("x");
        Var var66 = new Var("y");
        Var var67 = new Var("z");
        Var var68 = new Var("a");
        Var var69 = new Var("b");
        Var var70 = new Var("c");
        Var var71 = new Var("d");
        Val val58 = new Val(0);
        Val val59 = new Val(9);
        Val val60 = new Val(10);
        Val val61 = new Val(11);
        Cons cons17 = new Cons("MkPair", Arrays.asList(val59, val60));
        Cons cons18 = new Cons("MkPair", Arrays.asList(val59, val61));
        BinOp binOp14 = new BinOp(Op.EQ, var65, var70);
        BinOp binOp19 = new BinOp(Op.EQ, var65, var68);
        BinOp binOp15 = new BinOp(Op.PLUS, var68, var70);
        BinOp binOp16 = new BinOp(Op.PLUS, var69, var71);
        BinOp binOp17 = new BinOp(Op.TIMES, var68, var65);
        BinOp binOp18 = new BinOp(Op.TIMES, var69, var65);
        Cons cons19 = new Cons("MkPair", Arrays.asList(binOp15, binOp16));
        Cons cons20 = new Cons("MkPair", Arrays.asList(binOp17, binOp18));
        If ifcon11 = new If(binOp14, cons19, cons20);
        CaseAlt casealt17 = new CaseAlt("Nil", Arrays.asList(), val58);
        CaseAlt casealt18 = new CaseAlt("MkPair", Arrays.asList("c", "d"), ifcon11);
        Case casecon10 = new Case(var67, Arrays.asList(casealt17, casealt18));
        If ifcon12 = new If(binOp19, casecon10, val58);
        CaseAlt casealt20 = new CaseAlt("MkPair", Arrays.asList("a", "b"), ifcon12);
        Case casecon11 = new Case(var66, Arrays.asList(casealt17, casealt20));
        Function complex_case2_def = new Function("complex_case2", Arrays.asList("x", "y", "z"), casecon11);

        /* complex_case3(y, z, w) = case y of
                Nil() -> 0
                MkPair(a, b) -> case z of
                    Nil() -> 0
                    MkPair(c, d) -> case w of
                        Nil() -> 0
                        Cons(v, vs) -> Cons(a * c * v, vs) */
        Var var74 = new Var("y");
        Var var75 = new Var("z");
        Var var76 = new Var("w");
        Var var77 = new Var("a");
        Var var79 = new Var("c");
        Var var81 = new Var("v");
        Val val64 = new Val(0);
        Val val65 = new Val(9);
        Val val66 = new Val(10);
        Val val67 = new Val(11);
        Val val68 = new Val(12);
        BinOp binOp20 = new BinOp(Op.TIMES, var77, var79);
        BinOp binOp21 = new BinOp(Op.TIMES, binOp20, var81);
        Cons cons21 = new Cons("Nil", Arrays.asList());
        Cons cons22 = new Cons("Cons", Arrays.asList(val65, cons21));
        Cons cons23 = new Cons("Cons", Arrays.asList(val66, cons22));
        Cons cons24 = new Cons("Cons", Arrays.asList(val67, cons23));
        Cons cons25 = new Cons("Cons", Arrays.asList(val68, cons24));
        Cons cons26 = new Cons("Cons", Arrays.asList(binOp21, cons24));
        Cons cons27 = new Cons("MkPair", Arrays.asList(val65, val66));
        Cons cons28 = new Cons("MkPair", Arrays.asList(val67, val68));
        CaseAlt casealt19 = new CaseAlt("Nil", Arrays.asList(), val64);
        CaseAlt casealt21 = new CaseAlt("Cons", Arrays.asList("v", "vs"), cons26);
        Case casecon12 = new Case(var76, Arrays.asList(casealt19, casealt21));
        CaseAlt casealt22 = new CaseAlt("MkPair", Arrays.asList("c", "d"), casecon12);
        Case casecon13 = new Case(var75, Arrays.asList(casealt19, casealt22));
        CaseAlt casealt23 = new CaseAlt("MkPair", Arrays.asList("a", "b"), casecon13);
        Case casecon14 = new Case(var74, Arrays.asList(casealt19, casealt23));
        Function complex_case3_def = new Function("complex_case3", Arrays.asList("y", "z", "w"), casecon14);

        /*complex_let1(x, y, z) = if x < 5 
            then let a = x + y / z
                in a + z
            else let b = x * y / z
                in b + z */
        Var var84 = new Var("x");
        Var var85 = new Var("y");
        Var var86 = new Var("z");
        Var var87 = new Var("a");
        Var var88 = new Var("b");
        Val val69 = new Val(5);
        BinOp binOp22 = new BinOp(Op.LT, var84, val69);
        BinOp binOp23 = new BinOp(Op.PLUS, var84, var85);
        BinOp binOp24 = new BinOp(Op.DIVIDE, binOp23, var86);
        BinOp binOp25 = new BinOp(Op.PLUS, var87, var86);
        Let let1 = new Let("a", binOp24, binOp25);
        BinOp binOp26 = new BinOp(Op.TIMES, var84, var85);
        BinOp binOp27 = new BinOp(Op.DIVIDE, binOp26, var86);
        BinOp binOp28 = new BinOp(Op.PLUS, var88, var86);
        Let let2 = new Let("b", binOp27, binOp28);
        If ifcon13 = new If(binOp22, let1, let2);
        Function complex_let1_def = new Function("complex_let1", Arrays.asList("x", "y", "z"), ifcon13);

        /* complex_let2(x, z) = case z of 
                Nil() -> 0
                Cons(y, ys) -> let a = y + 30
                    in a * y - x */
        Var var95 = new Var("x");
        Var var96 = new Var("z");
        Var var97 = new Var("y");
        Var var99 = new Var("a");
        Val val80 = new Val(30);
        Val val81 = new Val(0);
        Val val83 = new Val(10);
        Val val84 = new Val(20);
        Val val85 = new Val(40);
        Cons cons29 = new Cons("Nil", Arrays.asList());
        Cons cons30 = new Cons("Cons", Arrays.asList(val85, cons29));
        Cons cons31 = new Cons("Cons", Arrays.asList(val84, cons30));
        Cons cons32 = new Cons("Cons", Arrays.asList(val83, cons31));
        BinOp binOp35 = new BinOp(Op.PLUS, var97, val80);
        BinOp binOp36 = new BinOp(Op.TIMES, var97, var99);
        BinOp binOp37 = new BinOp(Op.MINUS, binOp36, var95);
        Let let5 = new Let("a", binOp35, binOp37);
        CaseAlt casealt24 = new CaseAlt("Nil", Arrays.asList(), val81);
        CaseAlt casealt25 = new CaseAlt("Cons", Arrays.asList("y", "ys"), let5);
        Case casecon19 = new Case(var96, Arrays.asList(casealt24, casealt25));
        Function complex_let2_def = new Function("complex_let2", Arrays.asList("x", "z"), casecon19);

        /* let_cons1(x) = let y = x + 10
                        in Cons(y * y, ys) */
        Var var120 = new Var("x");
        Var var121 = new Var("y");
        Var var122 = new Var("ys");
        Val val100 = new Val(10);
        Val val101 = new Val(2);
        Val val102 = new Val(3);
        BinOp binOp40 = new BinOp(Op.PLUS, var120, val100);
        BinOp binOp41 = new BinOp(Op.TIMES, var121, var121);
        Cons cons49 = new Cons("Nil", Arrays.asList());
        Cons cons50 = new Cons("Cons", Arrays.asList(val101, cons49));
        Cons cons51 = new Cons("Cons", Arrays.asList(val102, cons50));
        Cons cons52 = new Cons("Cons", Arrays.asList(binOp41, cons51));
        Let let7 = new Let("y", binOp40, cons52);
        Function let_cons1_def = new Function("let_cons1", Arrays.asList("x"), let7);

        /* let_cons2(y) = let z = y 
                     in MkPair (z - 4, z + 4) */
        Var var123 = new Var("y");
        Var var124 = new Var("z");
        Val val103 = new Val(4);
        BinOp binOp42 = new BinOp(Op.MINUS, var124, val103);
        BinOp binOp43 = new BinOp(Op.PLUS, var124, val103);
        Cons cons53 = new Cons("MkPair", Arrays.asList(binOp42, binOp43));
        Let let8 = new Let("z", var123, cons53);
        Function let_cons2_def = new Function("let_cons2", Arrays.asList("y"), let8);

        /* let_fun0(x, a, b) = let z = x 
                        in case z of 
                            Nil() -> 0
                            MkPair(c, d) -> c * a */
        Var var108 = new Var("x");
        Var var109 = new Var("y");
        Var var110 = new Var("z");
        Var var111 = new Var("a");
        Var var112 = new Var("b");
        Var var113 = new Var("c");
        Var var114 = new Var("d");
        Val val90 = new Val(0);
        Cons cons37 = new Cons("MkPair", Arrays.asList(var111, var112));
        BinOp binOp38 = new BinOp(Op.TIMES, var113, var114);
        CaseAlt casealt28 = new CaseAlt("Nil", Arrays.asList(), val90);
        CaseAlt casealt29 = new CaseAlt("MkPair", Arrays.asList("c", "d"), binOp38);
        Case casecon21 = new Case(var110, Arrays.asList(casealt28, casealt29));
        Let let6 = new Let("z", var108, casecon21);
        Function let_fun0_def = new Function("let_fun0", Arrays.asList("x", "a", "b"), let6);

        /* let_fun1(x, a, b, c) = let z = x
                        in case z of 
                            Nil() -> 0
                            Cons(y, ys) -> y + a */
        Var var107 = new Var("x");  
        Var var106 = new Var("y");
        Var var105 = new Var("z");
        Var var102 = new Var("a");
        Var var103 = new Var("b");
        Var var104 = new Var("c");
        Val val86 = new Val(0);
        Cons cons33 = new Cons("Nil", Arrays.asList());
        Cons cons34 = new Cons("Cons", Arrays.asList(var102, cons33));
        Cons cons35 = new Cons("Cons", Arrays.asList(var103, cons34));
        Cons cons36 = new Cons("Cons", Arrays.asList(var104, cons35));
        BinOp binOp39 = new BinOp(Op.PLUS, var106, var102);
        CaseAlt casealt26 = new CaseAlt("Nil", Arrays.asList(), val86);
        CaseAlt casealt27 = new CaseAlt("Cons", Arrays.asList("y", "ys"), binOp39);
        Case casecon20 = new Case(var105, Arrays.asList(casealt26, casealt27));
        Let let3 = new Let("z", var107, casecon20);
        Function let_fun1_def = new Function("let_fun1", Arrays.asList("x", "a", "b", "c"), let3);

        /* let_fun2(x, y, z) = let a = if z > x
                    then x + y
                    else x - y
                in a + z / x + y */
        Var var89 = new Var("x");
        Var var90 = new Var("y");
        Var var91 = new Var("z");
        Var var92 = new Var("a");
        BinOp binOp29 = new BinOp(Op.GT, var91, var89);
        BinOp binOp30 = new BinOp(Op.PLUS, var89, var90);
        BinOp binOp31 = new BinOp(Op.MINUS, var89, var90);
        If ifcon14 = new If(binOp29, binOp30, binOp31);
        BinOp binOp32 = new BinOp(Op.PLUS, var92, var91);
        BinOp binOp33 = new BinOp(Op.PLUS, var89, var90);
        BinOp binOp34 = new BinOp(Op.DIVIDE, binOp32, binOp33);
        Let let4 = new Let("a", ifcon14, binOp34);
        Function let_fun2_def = new Function("let_fun2", Arrays.asList("x", "y", "z"), let4);

        /* fun(x) = let y = case x of
                Nil() -> 0
                Cons(z, zs) -> z + z
                in y + y */
        Var var23 = new Var("x");
        Var var24 = new Var("y");
        Var var25 = new Var("z");
        Val val11 = new Val(0);
        BinOp binOp6 = new BinOp(Op.PLUS, var25, var25);
        BinOp binOp7 = new BinOp(Op.PLUS, var24, var24);
        CaseAlt caseAlt7 = new CaseAlt("Nil", Arrays.asList(), val11);
        CaseAlt caseAlt8 = new CaseAlt("Cons", Arrays.asList("z", "zs"), binOp6);
        Case casecon5 = new Case(var23, Arrays.asList(caseAlt7, caseAlt8));
        Let let = new Let("y", casecon5, binOp7);
        Function fun_def = new Function("fun", Arrays.asList("x"), let);

        /* map(f, xs) = case xs of
                  Nil -> Nil
                  Cons(y,ys) -> Cons(f(y), map(f,ys)) */
        Var var18 = new Var("f");
        Var var19 = new Var("xs");
        Var var20 = new Var("map");
        Var var21 = new Var("y");
        Var var22 = new Var("ys");
        Call call4 = new Call(var18, Arrays.asList(var21));
        Call call5 = new Call(var20, Arrays.asList(var18, var22));
        Cons con4 = new Cons("Cons", Arrays.asList(call4, call5));
        Cons con5 = new Cons("Nil", Arrays.asList());
        CaseAlt casealt5 = new CaseAlt("Nil", Arrays.asList(), con5);
        CaseAlt casealt6 = new CaseAlt("Cons", Arrays.asList("y", "ys"), con4);
        Case casecon4 = new Case(var19, Arrays.asList(casealt5, casealt6));
        Function map_def = new Function("map", Arrays.asList("f", "xs"), casecon4);

        /*
         * Below is the list of fuction calls for all the functions above.
         */

        /* allDefs = [double_def, factorial_def, fst_def, snd_def, sum_def, testlist_def, map_def] */
        List<Function> all_defs = Arrays.asList(identity_def, double_def, complex_expr1_def, complex_expr2_def, factor_def, complex_if1_def, complex_if2_def, complex_if3_def, complex_if4_def, complex_case1_def, complex_case2_def, complex_case3_def, snd_def, sum_def, testlist_def, testpair_def, testnil_def, complex_let1_def, complex_let2_def, let_cons1_def, let_cons2_def, let_fun0_def, let_fun1_def, let_fun2_def, fun_def, map_def);

        /* main = identity(7) */
        Val val12 = new Val(7);
        Var var33 = new Var("identity");
        Call call8 = new Call(var33, Arrays.asList(val12));
        Program mkprog4 = new Program(all_defs, call8);

        toJava(mkprog4);

        /* main = double(3) */
        Val val2 = new Val(3);
        Var var2 = new Var("double");
        Call call = new Call(var2, Arrays.asList(val2));
        Program mkprog = new Program(all_defs, call);

        toJava(mkprog);

        /* main = compelx_expr1(15, 9)*/
        Val val13 = new Val(15);
        Val val14 = new Val(9);
        Var var34 = new Var("complex_expr1");
        Call call9 = new Call(var34, Arrays.asList(val13, val14));
        Program mkprog5 = new Program(all_defs, call9);

        toJava(mkprog5);

        /* main = complex_expr2(15, 9, 3, 2)*/
        Val val15 = new Val(15);
        Val val16 = new Val(9);
        Val val17 = new Val(3);
        Val val18 = new Val(2);
        Var var35 = new Var("complex_expr2");
        Call call10 = new Call(var35, Arrays.asList(val15, val16, val17, val18));
        Program mkprog6 = new Program(all_defs, call10);

        toJava(mkprog6);

        /* main = testlist() */
        Call call14 = new Call(var17, Arrays.asList());
        Program mkprog10 = new Program(all_defs, call14);

        toJava(mkprog10);

        /* main = testpair()*/
        Var var44 = new Var("testpair");
        Call call18 = new Call(var44, Arrays.asList());
        Program mkprog12 = new Program(all_defs, call18);

        toJava(mkprog12);

        /* main = testnil() */
        Var var46 = new Var("testnil");
        Call call19 = new Call(var46, Arrays.asList());
        Program mkprog13 = new Program(all_defs, call19);

        toJava(mkprog13);

        /* main = factorial(5) */
        Val val8 = new Val(5);
        Call call6 = new Call(var4, Arrays.asList(val8));
        Program mkprog2 = new Program(all_defs, call6);

        toJava(mkprog2);

        /* main = complex_if1(6, 11) */
        Val val24 = new Val(6);
        Val val25 = new Val(11);
        Var var38 = new Var("complex_if1");
        Call call11 = new Call(var38, Arrays.asList(val24, val25));
        Program mkprog7 = new Program(all_defs, call11);

        toJava(mkprog7);

        /* main = complex_if2(2, 4, 6) */
        Val val31 = new Val(2);
        Val val32 = new Val(4);
        Val val33 = new Val(6);
        Var var42 = new Var("complex_if2");
        Call call12 = new Call(var42, Arrays.asList(val31, val32, val33));
        Program mkprog8 = new Program(all_defs, call12);

        toJava(mkprog8);

        /* main = complex_if3(5, Cons 1 (Cons 2 (Cons 3 Nil)) */
        Val val47 = new Val(3);
        Var var53 = new Var("complex_if3");
        Call call20 = new Call(var53, Arrays.asList(val47, cons7));
        Program mkprog14 = new Program(all_defs, call20);

        toJava(mkprog14);

        /* main = complex_if4(8, 8, (Cons 5 (Cons 7 Nil))) */
        Var var59 = new Var("complex_if4");
        Call call21 = new Call(var59, Arrays.asList(val48, val48, cons10));
        Program mkprog15 = new Program(all_defs, call21);

        toJava(mkprog15);

        /* main = snd(MkPair(1,2)) */
        Val val62 = new Val(1);
        Val val63 = new Val(2);
        Cons con6 = new Cons("MkPair", Arrays.asList(val62, val63));
        Var var48 = new Var("snd");
        Call call1 = new Call(var48, Arrays.asList(con6));
        Program mkprog1 = new Program(all_defs, call1);

        toJava(mkprog1);

        /* main = sum(Cons 5 (Cons 2 (Cons 4 (Cons 7 Nil)))) */
        Val val36 = new Val(5);
        Val val37 = new Val(2);
        Val val38 = new Val(4);
        Val val39 = new Val(7);
        Cons con10 = new Cons("Nil", Arrays.asList());
        Cons con11 = new Cons("Cons", Arrays.asList(val39, con10));
        Cons con12 = new Cons("Cons", Arrays.asList(val38, con11));
        Cons con13 = new Cons("Cons", Arrays.asList(val37, con12));
        Cons con14 = new Cons("Cons", Arrays.asList(val36, con13));
        Var var43 = new Var("sum");
        Call call13 = new Call(var43, Arrays.asList(con14));
        Program mkprog9 = new Program(all_defs, call13);

        toJava(mkprog9);

        /* main = complex_case1(4, Cons 4 (Cons 5 (Cons 6 Nil))) */
        Call call23 = new Call(var63, Arrays.asList(val56, cons15));
        Program mkprog16 = new Program(all_defs, call23);

        toJava(mkprog16);

        /* main = complex_case2(9, MkPair(9, 10), MkPair(9, 11)) */
        Var var72 = new Var("complex_case2");
        Call call24 = new Call(var72, Arrays.asList(val59, cons17, cons18));
        Program mkprog17 = new Program(all_defs, call24);

        toJava(mkprog17);

        /* main = complex_case3(MkPair(9, 10), MkPair(11, 12), Cons 9 (Cons 10 (Cons 11 (Cons 12 Nil)))) */
        Var var83 = new Var("complex_case3");
        Call call25 = new Call(var83, Arrays.asList(cons27, cons28, cons25));
        Program mkprog18 = new Program(all_defs, call25);

        toJava(mkprog18);

        /* main = complex_let1(10, 2, 5) */
        Val val70 = new Val(10);
        Val val71 = new Val(2);
        Val val72 = new Val(5);
        Var var80 = new Var("complex_let1");
        Call call26 = new Call(var80, Arrays.asList(val70, val71, val72));
        Program mkprog19 = new Program(all_defs, call26);

        toJava(mkprog19);

        /* main = complex_let2() */
        Val val79 = new Val(34);
        Var var100 = new Var("complex_let2");
        Call call29 = new Call(var100, Arrays.asList(val79, cons32));
        Program mkprog22 = new Program(all_defs, call29);

        toJava(mkprog22);

        /* main = let_cons1(2) */
        Val val82 = new Val(2);
        Var var101 = new Var("let_cons1");
        Call call31 = new Call(var101, Arrays.asList(val82));
        Program mkprog24 = new Program(all_defs, call31);

        toJava(mkprog24);

        /* main = let_cons2(21) */
        Val val91 = new Val(21);
        Var var115 = new Var("let_cons2");
        Call call32 = new Call(var115, Arrays.asList(val91));
        Program mkprog25 = new Program(all_defs, call32);

        toJava(mkprog25);

        /* main = let_fun0(MkPair(a, b), 7, 8) */
        Val val77 = new Val(7);
        Val val78 = new Val(8);
        Var var98 = new Var("let_fun0");
        Call call30 = new Call(var98, Arrays.asList(cons37, val77, val78));
        Program mkprog23 = new Program(all_defs, call30);

        toJava(mkprog23);

        /* main = let_fun1(Cons c (Cons b (Cons a Nil)), 3, 9, 7) */
        Val val88 = new Val(3);
        Val val87 = new Val(9);
        Val val89 = new Val(17);
        Var var94 = new Var("let_fun1");
        Call call28 = new Call(var94, Arrays.asList(cons36, val88, val87, val89));
        Program mkprog21 = new Program(all_defs, call28);

        toJava(mkprog21);

        /* main = let_fun2(5, 10, 15) */
        Val val74 = new Val(5);
        Val val75 = new Val(10);
        Val val76 = new Val(15);
        Var var93 = new Var("let_fun2");
        Call call27 = new Call(var93, Arrays.asList(val74, val75, val76));
        Program mkprog20 = new Program(all_defs, call27);

        toJava(mkprog20);

        /* main = fun(Cons 1 (Cons 2 Nil)) */
        Val val34 = new Val(1);
        Val val35 = new Val(2);
        Cons con7 = new Cons("Nil", Arrays.asList());
        Cons con8 = new Cons("Cons", Arrays.asList(val35, con7));
        Cons con9 = new Cons("Cons", Arrays.asList(val34, con8));
        Var var47 = new Var("fun");
        Call call17 = new Call(var47, Arrays.asList(con9));
        Program mkprog11 = new Program(all_defs, call17);

        toJava(mkprog11);

        /* main = sum(map(double, testlist)) */
        Var var45 = new Var("map");
        Call call15 = new Call(var45, Arrays.asList(var2, var17));
        Call call16 = new Call(var43, Arrays.asList(call15));
        Program testProg5 = new Program(all_defs, call16);

        //toJava(testProg5);
    }

    //takes the mkprog and converts it to java code by calling ANFConversion and GenerateJava class
    public static void toJava(Program mkprog) {
        Map<Expr, Expr> mainANF;
        Map<Expr, Expr> funcANF;
        Map<String, List<String>> mainANFStr;
        Map<String, List<String>> funcANFStr;
        Map<String, Expr> argMap = new HashMap<>();

        Call mbody = (Call) mkprog.getExpression();
        Var funCall = (Var) mbody.getFunction();
        List<Expr> params = mbody.getArguments();
        String funCallName = funCall.getName();
        Function func = null;
        String funcName = "";
        Object result;

        for (Function function : mkprog.getFunctions()) {
            func = function;
            funcName = function.getName();

            if (funCallName.equals(funcName)) {
                break;
            }
        }

        List<String> args = func.getArguments();
        Expr fBody = func.getBody();

        if (params.size() != 0) {
            for(int i = 0; i < args.size(); i++) {
                argMap.put(args.get(i), params.get(i));
            }   
        }

        ANFConversion mainBody = new ANFConversion();
        ANFConversion funcBody = new ANFConversion();

        Expr anfMainBody = mainBody.progToANF(mbody);
        Expr anfFuncBody = funcBody.progToANF(fBody);

        mainBody.formatSubExprs();
        funcBody.formatSubExprs();

        mainANF = mainBody.getAnfMap();
        mainANFStr = mainBody.getStrMap();

        funcANF = funcBody.getAnfMap();
        funcANFStr = funcBody.getStrMap();

        GenerateJava genJavaMain = new GenerateJava(mainANF, mainANFStr);
        GenerateJava genJavaFunc = new GenerateJava(funcANF, funcANFStr);

        genJavaMain.printMainFunc(funCallName, params, anfMainBody);
        genJavaFunc.printFunc(funcName, args, anfFuncBody);

        result = genJavaFunc.evaluteFunc(argMap, anfFuncBody, mkprog);

        if (result instanceof Integer) {
            System.out.println("Output: " + result);
        } else if (result instanceof Cons) {
            Cons cons = (Cons) result;
            String consName = cons.getName();
            List<Expr> consArgs = cons.getArguments();

            if (consName.equals("Nil")) {
                System.out.println("Output: " + cons.getName() + "()");
            } else if (consName.equals("MkPair")) {
                Val fst = (Val) consArgs.get(0);
                Val snd = (Val) consArgs.get(1);

                System.out.println("Output: " + cons.getName() + "(" + fst.getValue() + ", " + snd.getValue() + ")");
            } else if (consName.equals("Cons")) {
                String outputStr = printListCons(cons);
                System.out.println("Output: " + outputStr);
            }
        }
    }

    //prints the output list constructor
    public static String printListCons(Cons cons) {
        List<Expr> consArgs = cons.getArguments();
        Expr head = null;
        Expr tail = null;
        int headVal = 0;
        String tailStr = "";

        if (consArgs.size() > 1) {
            head = consArgs.get(0);
            tail = consArgs.get(1);
        }

        if (head instanceof Val) {
            Val val = (Val) head;
            headVal = val.getValue();
        }

        if (tail instanceof Cons) {
            tailStr = printListCons((Cons) tail);
        } else if (tail instanceof Val) {
            tailStr = tail.toString();
        } else {
            tailStr = "Nil()";
        }

        if (consArgs.size() == 0) {
            return "Nil()";
        } else {
            return "Cons(" + headVal + ", " + tailStr + ")";
        }
    }
}