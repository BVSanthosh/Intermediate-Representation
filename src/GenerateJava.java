import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateJava {

    private Map<Expr, Expr> anfExprMap = new HashMap<>();
    private Map<String, List<String>> anfStrMap = new HashMap<>();

    public GenerateJava(Map<Expr, Expr> anfExprMAp, Map<String, List<String>> anfStrMap) {
        this.anfExprMap = anfExprMAp;
        this.anfStrMap = anfStrMap; 
    }

    //prints the main method
    public void printMainFunc(String call, List<Expr> param, Expr body) {
        String mainFuncStr = "";

        mainFuncStr += "public static void main(String[] args) {" + "\n";

        if (anfExprMap.keySet().size() > 0) {
            String tempVarStr = "   Object ";

            if (anfExprMap.keySet().size() == 1) {
                for (Expr key : anfExprMap.keySet()) {
                    ANFVar var = (ANFVar) key;
                    tempVarStr += var.getName() + ";" + "\n";
                }

                mainFuncStr += tempVarStr;
            } else {
                int count = 0;

                for (Expr key : anfExprMap.keySet()) {
                    ANFVar var = (ANFVar) key;

                    if (count == 0) {
                        tempVarStr += var.getName();
                        count++;
                    } else {
                        tempVarStr += ", " + var.getName();
                    }
                }

                tempVarStr += ";" + "\n";
                mainFuncStr += tempVarStr;
            }
        }

        if (body instanceof ANFVar) {
            ANFVar anfvar = (ANFVar) body;
            String subExpr = getSubExpr(anfvar);
            mainFuncStr += "   ";
            mainFuncStr += subExpr;
        } else {
            mainFuncStr += getFuncBody(body);
        }

        mainFuncStr += "}";

        System.out.println(mainFuncStr);
    }

    //prints the function
    public void printFunc(String name, List<String> args, Expr body) {
        String funcStr = "";
        String argList = "";

        if (args.size() != 0) {
            if (args.size() == 1) {
                argList = "Object " + args.get(0);
            } else {
                argList = "Object " + args.get(0);

                for (int count = 1; count < args.size(); count++) {
                argList += ", Object " + args.get(count);
                }
            }
        }

        funcStr  += "Object " + name + "(" + argList + ") {" + "\n";

        if (anfExprMap.keySet().size() > 0) {
            String tempVarStr = "   Object ";

            if (anfExprMap.keySet().size() == 1) {
                for (Expr key : anfExprMap.keySet()) {
                    ANFVar var = (ANFVar) key;
                    tempVarStr += var.getName() + ";" + "\n";
                }

                funcStr += tempVarStr;
            } else {
                int count = 0;

                for (Expr key : anfExprMap.keySet()) {
                    ANFVar var = (ANFVar) key;

                    if (count == 0) {
                        tempVarStr += var.getName();
                        count++;
                    } else {
                        tempVarStr += ", " + var.getName();
                    }
                }

                tempVarStr += ";" + "\n";
                funcStr += tempVarStr;
            }
        }


        if (body instanceof ANFVar) {
            ANFVar anfvar = (ANFVar) body;
            String subExpr = getSubExpr(anfvar);
            funcStr += "   ";
            funcStr += subExpr;
            funcStr += "   return " + anfvar.getName() + ";" + "\n";
        } else if (body instanceof Var) {
            Var var = (Var) body;
            funcStr += "   return " + var.getName() + ";" + "\n";
        } else if (body instanceof Val) {
            Val val = (Val) body;
            funcStr += "   return " + val.getValue() + ";" + "\n";
        } else {
            funcStr += getFuncBody(body);
        }

        funcStr += "}";

        System.out.println(funcStr);
    }

    //resursive function for printing the function body for both the main method and the function
    public String getFuncBody(Expr body) {
        String funcBody = "";

        switch (body.getClass().getSimpleName()) {
            case "Val":
                Val val = (Val) body;
                int num = val.getValue();

                return Integer.toString(num);

            case "Var":
                Var var = (Var) body;
                String name = var.getName();
                
                return name;
            case "ANFVar":
                ANFVar anfvar = (ANFVar) body;
                String anfvarName = anfvar.getName();

                return anfvarName;
            case "BinOp":
                BinOp binOp = (BinOp) body;
                String left = getFuncBody(binOp.getLeft());
                String right = getFuncBody(binOp.getRight());
                String op = binOp.getOperationStr();
                String subExpr = "";

                if (binOp.getLeft() instanceof ANFVar) {
                    subExpr = getSubExpr((ANFVar) binOp.getLeft());
                    funcBody += subExpr;
                }

                if (binOp.getRight() instanceof ANFVar) {
                    subExpr = getSubExpr((ANFVar) binOp.getRight());
                    funcBody += subExpr;
                }

                return funcBody += "   return " + left + " " + op + " " + right + ";";
            case "If":
                If ifcon = (If) body;
                String ifCondStr = getFuncBody(ifcon.getCondition());
                String ifThenStr = getFuncBody(ifcon.getThenExpr());
                String ifElseStr = getFuncBody(ifcon.getElseExpr());
                String subExpr2 = "";
                String subExpr3 = "";
                String subExpr4 = "";

                if (ifcon.getCondition() instanceof ANFVar) {
                    subExpr2 = getSubExpr( (ANFVar) ifcon.getCondition());
                    funcBody += subExpr2;
                }

                funcBody += "   if (" + ifCondStr + ") {" + "\n";

                if (ifcon.getThenExpr() instanceof ANFVar) {
                    subExpr3 = getSubExpr( (ANFVar) ifcon.getThenExpr());
                    funcBody += "      " + subExpr3;
                }

                if (ifcon.getThenExpr() instanceof If || ifcon.getThenExpr() instanceof Case || ifcon.getThenExpr() instanceof Let) {
                    funcBody += "      " + ifThenStr + "\n";
                } else {
                    funcBody += "      return " + ifThenStr + ";" + "\n";
                }

                funcBody += "   } else {" + "\n";

                if (ifcon.getElseExpr()instanceof ANFVar) {
                    subExpr4 = getSubExpr( (ANFVar) ifcon.getElseExpr());
                    funcBody += "      " + subExpr4;
                }

                if (ifcon.getElseExpr() instanceof If || ifcon.getElseExpr() instanceof Case || ifcon.getElseExpr() instanceof Let) {
                    funcBody += "      " + ifElseStr + "\n";
                } else {
                    funcBody += "      return " + ifElseStr + ";" + "\n";
                }

                funcBody += "   }" + "\n";

                return funcBody;
            case "Call":
                Call call = (Call) body;
                Var callFunc = (Var) call.getFunction();
                String callFuncName = callFunc.getName();
                List<Expr> callArgs = call.getArguments();
                List<String> callArgStr = new ArrayList<>();
                String subExpr5 = "";

                for (Expr arg : callArgs) {
                    callArgStr.add(getFuncBody(arg));

                    if (arg instanceof ANFVar) {
                        subExpr5 = getSubExpr((ANFVar) arg);
                        funcBody += subExpr5;
                    }
                }
                
                String callArgList = "";

                if (callArgStr.size() == 1) {
                    callArgList = callArgStr.get(0);
                } else {
                    callArgList = callArgStr.get(0);

                    for (int count = 0; count < callArgStr.size(); count++) {
                        callArgList += ", " + callArgStr.get(count);
                    }
                }

                return funcBody += callFuncName + "(" + callArgList + ");";   
            case "Case":
                Case casecon = (Case) body;
                String caseExprStr = getFuncBody(casecon.getExpression());
                List<CaseAlt> caseAlts = casecon.getAlternatives();
                String subExpr6 = "";

                if (casecon.getExpression() instanceof ANFVar) {
                    subExpr6 = getSubExpr( (ANFVar) casecon.getExpression());
                    funcBody += subExpr6;
                }

                funcBody += "   switch (" + caseExprStr + ") {" + "\n";

                for (CaseAlt casealt : caseAlts) {
                    String casealtName = casealt.getName();
                    List<String> binders = casealt.getBinders();
                    String casealtExprStr = getFuncBody(casealt.getExpression());
                    String subExpr7 = "";

                    funcBody += "      case " + "\"" + casealtName + "\"" + ":" + "\n";

                    for (String binder : binders) {
                        funcBody += binder + " = " + caseExprStr + ".getArguments().get(" + binders.indexOf(binder) + ");" + "\n";
                    }

                    if (casealt.getExpression() instanceof ANFVar) {
                        subExpr7 = getSubExpr( (ANFVar) casealt.getExpression());
                        funcBody += subExpr7;
                    }

                    if (casealt.getExpression() instanceof If || casealt.getExpression() instanceof Case || casealt.getExpression() instanceof Let) {
                        funcBody += "         " + casealtExprStr + "\n";
                    } else {
                        funcBody += "         " + "return " + casealtExprStr + ";" + "\n";
                    }
                }

                funcBody += "   }" + "\n";

                return funcBody; 

            case "Cons":
                Cons cons = (Cons) body;
                String consName = cons.getName();
                List<Expr> consArgs = cons.getArguments();
                List<String> consArgStr = new ArrayList<>();

                for (Expr arg : consArgs) {
                    consArgStr.add(getFuncBody(arg));
                }

                String consArgList = "";

                if (consArgStr.size() == 1) {
                    consArgList = consArgStr.get(0);
                } else {
                    consArgList = consArgStr.get(0);

                    for (int count = 1; count < consArgStr.size(); count++) {
                        consArgList += ", " + consArgStr.get(count);
                    }
                }
                                                                                                
                return funcBody = "   " + consName + "(" + consArgList + ")";
            case "Let":
                Let let = (Let) body;
                String letName = let.getName();
                String letBoundExprStr = getFuncBody(let.getBoundExpr());
                String letBodyStr = getFuncBody(let.getBody());
                String subExpr8 = "";
                String subExpr9 = "";

                if (let.getBoundExpr() instanceof ANFVar) {
                    subExpr9 = getSubExpr( (ANFVar) let.getBoundExpr());
                    funcBody += subExpr9;
                }

                funcBody += "   Object " + letName + " = " + letBoundExprStr + ";" + "\n";

                if (let.getBody() instanceof ANFVar) {
                    subExpr8 = getSubExpr( (ANFVar) let.getBody());
                    funcBody += subExpr8;
                }

                funcBody += "return " + letBodyStr + ";" + "\n";

                return funcBody;
            default:
                break;
        }
        
        return funcBody;
    }

    //gets all sub-expressions list every time an ANFVar is encountered
    public String getSubExpr(ANFVar var) {
        String subExpr = "";
        
        for (String key : anfStrMap.keySet()) {
            if (key.equals(var.getName())) {
                List<String> subExprList = anfStrMap.get(key);

                for (int i = subExprList.size() - 1; i >= 0; i--) {
                    subExpr += subExprList.get(i) + "\n";
                }
            }
        }

        return subExpr;
    }

    //resursive function for evaluating the function body
    public Object evaluteFunc(Map<String, Expr> paraMap, Expr body, Program prog) {
        Object result = 0;

        switch (body.getClass().getSimpleName()) {
            case "Val":
                Val val = (Val) body;
                int num = val.getValue();

                return num;
            case "Var":
                Var var = (Var) body;
                String name = var.getName();

                for (String key : paraMap.keySet()) {
                    if (key.equals(name)) {
                        Expr expr = paraMap.get(key);

                        if (expr instanceof Val) {
                            Val val2 = (Val) expr;
                            return val2.getValue();
                        } else {
                            return paraMap.get(key);
                        }
                    }
                }
            case "ANFVar":
                ANFVar anfvar = (ANFVar) body;

                for (Expr varName : anfExprMap.keySet()) {
                    ANFVar keyName = (ANFVar) varName;

                    if (keyName.getName().equals(anfvar.getName())) {
                        Expr varExpr = anfExprMap.get(varName);
                        Object varExprRes = evaluteFunc(paraMap, varExpr, prog);
            
                        return varExprRes;
                    }
                }
            case "BinOp":
                BinOp binop = (BinOp) body;
                Expr left = binop.getLeft();
                Expr right = binop.getRight();
                int leftRes = (Integer) evaluteFunc(paraMap, left, prog);
                int rightRes = (Integer) evaluteFunc(paraMap, right, prog);
                String op = binop.getOperationStr();

                switch (op) {
                    case "+":
                        result = leftRes + rightRes;
                        break;
                    case "-":
                        result = leftRes - rightRes;
                        break;
                    case "*":
                        result = leftRes * rightRes;
                        break;
                    case "/":
                        result = leftRes / rightRes;
                        break;
                    case "==":
                        if (leftRes == rightRes) {
                            result = 1;
                        } else {
                            result = 0;
                        }
                        break;
                    case "<":
                        if (leftRes < rightRes) {
                            result = 1;
                        } else {
                            result = 0;
                        }
                        break;
                    case ">":
                        if (leftRes > rightRes) {
                            result = 1;
                        } else {
                            result = 0;
                        }
                        break;
                    default:
                        break;
                }

                return result;
            case "If":
                If ifcon = (If) body;
                Expr ifCon = ifcon.getCondition();
                Expr ifThen = ifcon.getThenExpr();
                Expr ifElse = ifcon.getElseExpr();
                int ifConRes = (Integer) evaluteFunc(paraMap, ifCon, prog);

                if (ifConRes == 1) {
                    Object ifThenRes = evaluteFunc(paraMap, ifThen, prog);
                    result = ifThenRes;
                } else {
                    Object ifElseRes = evaluteFunc(paraMap, ifElse, prog);
                    result = ifElseRes;
                }

                return result;
            case "Case":
                Case casecon = (Case) body;
                Expr caseExpr = casecon.getExpression();
                List<CaseAlt> caseAlts = casecon.getAlternatives();
                Cons caseExprRes = (Cons) evaluteFunc(paraMap, caseExpr, prog);
                String caseExprName = caseExprRes.getName();
                List<Expr> caseExprArgs = caseExprRes.getArguments();

                switch (caseExprName) {
                    case "MkPair":
                        for (CaseAlt caseAlt : caseAlts) {
                            if (caseAlt.getName().equals("MkPair")) {
                                Expr caseAltExpr = caseAlt.getExpression();
                                List<String> binders = caseAlt.getBinders();
                                List<Expr> caseExprArgsRes = new ArrayList<>();
                                Object tempExpr = null;

                                for (Expr expr : caseExprArgs) {
                                    if (expr instanceof Val) {
                                        Val val2 = (Val) expr;
                                        caseExprArgsRes.add(val2);
                                    } else if (expr instanceof Var) {
                                        Var var2 = (Var) expr;
                                        tempExpr = evaluteFunc(paraMap, var2, prog);

                                        if (tempExpr instanceof Integer) {
                                            caseExprArgsRes.add(new Val((Integer) tempExpr));
                                        } 
                                    } else if (expr instanceof ANFVar) {
                                        ANFVar anfvar2 = (ANFVar) expr;
                                        tempExpr = evaluteFunc(paraMap, anfvar2, prog);

                                        if (tempExpr instanceof Integer) {
                                            caseExprArgsRes.add(new Val((Integer) tempExpr));
                                        } 
                                    }
                                }
                                for (int count = 0; count < binders.size(); count++) {
                                    paraMap.put(binders.get(count), caseExprArgsRes.get(count));
                                }

                                Object caseAltExprRes = evaluteFunc(paraMap, caseAltExpr, prog);
                                result = caseAltExprRes;
                            }
                        }

                        return result;
                    case "Nil":
                        for (CaseAlt caseAlt : caseAlts) {
                            if (caseAlt.getName().equals("Nil")) {
                                Expr caseAltExpr2 = caseAlt.getExpression();
                                Object caseAltExprRes2 = evaluteFunc(paraMap, caseAltExpr2, prog);
                                result = caseAltExprRes2;
                            }
                        }

                        return result;
                    case "Cons":
                        for (CaseAlt caseAlt : caseAlts) {
                            if (caseAlt.getName().equals("Cons")) {
                                Expr caseAltExpr3 = caseAlt.getExpression();
                                List<String> binders2 = caseAlt.getBinders();
                                Expr head = caseExprArgs.get(0);
                                Val headVal = null;
                                Object tempHead = null;

                                if (head instanceof Val) {
                                    Val val2 = (Val) head;
                                    headVal = val2;
                                } else if (head instanceof Var) {
                                    Var var2 = (Var) head;
                                    tempHead = evaluteFunc(paraMap, var2, prog);

                                    if (tempHead instanceof Integer) {
                                        headVal = new Val((Integer) tempHead);
                                    } 
                                } else if (head instanceof ANFVar) {
                                    ANFVar anfvar2 = (ANFVar) head;
                                    tempHead = evaluteFunc(paraMap, anfvar2, prog);

                                    if (tempHead instanceof Integer) {
                                        headVal = new Val((Integer) tempHead);
                                    } 
                                }

                                Cons tail = (Cons) caseExprArgs.get(1);

                                paraMap.put(binders2.get(0), headVal);
                                paraMap.put(binders2.get(1), tail);

                                Object caseAltExprRes3 = evaluteFunc(paraMap, caseAltExpr3, prog);

                                if (caseAltExprRes3 instanceof Integer) {
                                    result = (Integer) caseAltExprRes3;
                                } else if (caseAltExprRes3 instanceof Cons) {
                                    result = (Cons) caseAltExprRes3;
                                }
                            }
                        }

                        return result;
                    default:
                        break;
                }
                return result; 
            case "Call":
                Call call = (Call) body;
                Var callFunc = (Var) call.getFunction();
                String callFuncName = callFunc.getName();
                List<Expr> callArgs = call.getArguments();
                List<Function> funcs = prog.getFunctions();
                List<String> funcArgs = new ArrayList<>();
                List<Expr> callArgsRes = new ArrayList<>();
                Expr funcBody = null;

                for (Function func : funcs) {
                    if (func.getName().equals(callFuncName)) {
                        funcArgs = func.getArguments();
                        funcBody = func.getBody();
                        break;
                    }
                }

                for (Expr arg : callArgs) {
                    Object argRes = evaluteFunc(paraMap, arg, prog);

                    if (argRes instanceof Integer) {
                        Val val4 = new Val((Integer)argRes);
                        callArgsRes.add(val4);
                    } else if (argRes instanceof Cons) {
                        Cons cons = (Cons) argRes;
                        callArgsRes.add(cons);
                    }
                }

                for (int i = 0; i < funcArgs.size(); i++) {
                    paraMap.put(funcArgs.get(i), callArgsRes.get(i));
                }

                result = (Integer) evaluteFunc(paraMap, funcBody, prog);

                return result;
            case "Cons":
                Cons cons = (Cons) body;
                String consName = cons.getName();
                List<Expr> consArgs = cons.getArguments();
                List<Expr> evaluatedArgs = new ArrayList<>();

                switch (consName) {
                    case "Nil":
                        return new Cons(consName, consArgs);
                    case "MkPair":
                        Object fst = evaluteFunc(paraMap, consArgs.get(0), prog);
                        Object snd = evaluteFunc(paraMap, consArgs.get(1), prog);
                        Val val3 = null;
                        Val val4 = null;

                        if (fst instanceof Integer) {
                            val3 = new Val((Integer) fst);
                            evaluatedArgs.add(val3);
                        } else if (fst instanceof Val) {
                            evaluatedArgs.add((Val) fst);
                        }

                        if (snd instanceof Integer) {
                            val4 = new Val((Integer) snd);
                            evaluatedArgs.add(val4);
                        } else if (snd instanceof Val) {
                            evaluatedArgs.add((Val) snd);
                        }

                        return new Cons(consName, evaluatedArgs);
                    case "Cons":
                    Object argRes = null;
                    Val val2 = null;
                    Cons cons2 = null;

                        for (Expr arg : consArgs) {
                            if (arg instanceof Val) {
                                evaluatedArgs.add(arg); 
                            } else if (arg instanceof Var) {
                                argRes = evaluteFunc(paraMap, arg, prog);

                                if (argRes instanceof Integer) {
                                    val2 = new Val((Integer)argRes);
                                    evaluatedArgs.add(val2);
                                } else if (argRes instanceof Cons) {
                                    cons2 = (Cons) argRes;
                                    evaluatedArgs.add(cons2);
                                }
                            } else if (arg instanceof ANFVar) {
                                argRes = evaluteFunc(paraMap, arg, prog);

                                if (argRes instanceof Integer) {
                                    val2 = new Val((Integer)argRes);
                                    evaluatedArgs.add(val2);
                                } else if (argRes instanceof Cons) {
                                    cons2 = (Cons) argRes;
                                    evaluatedArgs.add(cons2);
                                }
                            }
                        }

                        return new Cons(consName, evaluatedArgs);
                    default:
                        return result;
                }
            case "Let":
                Let let = (Let) body;
                String letName = let.getName();
                Expr letBoundExpr = let.getBoundExpr();
                Expr letBody = let.getBody();
                Object letBoundExprRes = evaluteFunc(paraMap, letBoundExpr, prog);

                if (letBoundExprRes instanceof Integer) {
                    Val val2 = new Val((Integer)letBoundExprRes);
                    paraMap.put(letName, val2);
                } else if (letBoundExprRes instanceof Cons) {
                    paraMap.put(letName, (Cons) letBoundExprRes);
                }

                Object letBodyRes = evaluteFunc(paraMap, letBody, prog);

                if (letBodyRes instanceof Integer) {
                    result = (Integer) letBodyRes;
                } else if (letBodyRes instanceof Cons) {
                    result = (Cons) letBodyRes;
                }

                return letBodyRes;
            default:
                break;
        }

        return result;
    }
}