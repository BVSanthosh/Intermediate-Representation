import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ANFConversion {
    
    private Map<Expr, Expr> anfExprMap = new HashMap<>();
    private Map<String, List<String>> anfStrMap = new HashMap<>();
    private int tempVarCount = 0;

    public Map<Expr, Expr> getAnfMap() {
        return anfExprMap;
    }

    public Map<String, List<String>> getStrMap() {
        return anfStrMap;
    }

    //resursive function for converting a program to ANF
    public Expr progToANF(Expr expr) {
        Expr anfExpr = null;

        switch (expr.getClass().getSimpleName()) {
            case "Val":
                return expr;
            case "Var":
                return expr;
            case "BinOp":
                BinOp binop = (BinOp) expr;
                Expr anfLeft = progToANF(binop.getLeft());
                Expr anfRight = progToANF(binop.getRight());
                BinOp newBinOp = new BinOp(binop.getOperation(), anfLeft, anfRight);

                ANFVar anfVar = new ANFVar("temp" + tempVarCount++);
                anfExprMap.put(anfVar, newBinOp);

                return anfVar;
            case "If":
                If ifcon = (If) expr;
                Expr ifConANF = progToANF(ifcon.getCondition());
                Expr ifThenANF = progToANF(ifcon.getThenExpr());
                Expr ifElseANF = progToANF(ifcon.getElseExpr());

                return new If(ifConANF, ifThenANF, ifElseANF);
            case "Case":
                Case casecon = (Case) expr;
                Expr caseExprANF = progToANF(casecon.getExpression());
                List<CaseAlt> caseAltsANF = new ArrayList<>();

                for (CaseAlt casealt : casecon.getAlternatives()) {
                    List<String> binders = casealt.getBinders();

                    for (String binder : binders) {
                        anfExprMap.put(new ANFVar(binder), new ANFVar(binder));
                    }

                    Expr casealtExprANF = progToANF(casealt.getExpression());;
                    caseAltsANF.add(new CaseAlt(casealt.getName(), casealt.getBinders(), casealtExprANF));
                }

                return new Case(caseExprANF, caseAltsANF);
            case "Let":
                Let letExpr = (Let) expr;
                Expr boundExprANF = progToANF(letExpr.getBoundExpr());
                Expr bodyExprANF = progToANF(letExpr.getBody());

                return new Let(letExpr.getName(), boundExprANF, bodyExprANF);
            case "Call": 
                Call call = (Call) expr;
                Expr callFuncANF = progToANF(call.getFunction());
                List<Expr> callArgsANF = new ArrayList<>();

                for (Expr arg : call.getArguments()) {
                    callArgsANF.add(progToANF(arg));
                }

                Call anfCall = new Call(callFuncANF, callArgsANF);

                anfVar = new ANFVar("temp" + tempVarCount++);
                anfExprMap.put(anfVar, anfCall);
                
                return anfVar;
            case "Cons":
                Cons consExpr = (Cons) expr;
                List<Expr> consArgsANF = new ArrayList<>();

                for (Expr arg : consExpr.getArguments()) {
                    consArgsANF.add(progToANF(arg));
                }

                Cons anfCons = new Cons(consExpr.getName(), consArgsANF);

                anfVar = new ANFVar("temp" + tempVarCount++);
                anfExprMap.put(anfVar, anfCons);

                return anfVar;
            default:
                break;
        }

        return anfExpr;
    }

    //formats the sub-expressions generaetd from progToANF() into a list of strings for each ANF variable
    public void formatSubExprs() {
        for (Expr key : anfExprMap.keySet()){
            ANFVar tempVar = (ANFVar) key;
            Expr subExpression = anfExprMap.get(key);
            List<String> strList = new ArrayList<>();

            strList = getSubExprs(tempVar.getName(), subExpression, strList);
            anfStrMap.put(tempVar.getName(), strList);
        }
    }

    //recursive function for getting the sub-expressions for each ANF variable
    public List<String> getSubExprs(String name, Expr subExpression, List<String> exprList) {
        String bodyStr = getSubExprStr(subExpression);
        exprList.add(name + " = " + bodyStr);

        switch (subExpression.getClass().getSimpleName()) {
            case "BinOp":
                BinOp binop = (BinOp) subExpression;
                Expr left = binop.getLeft();
                Expr right = binop.getRight();

                if (left instanceof ANFVar) {
                    ANFVar anfVar = (ANFVar) left;

                    for (Expr varName : anfExprMap.keySet()) {
                        ANFVar keyName = (ANFVar) varName;

                        if (keyName.getName().equals(anfVar.getName())) {
                            Expr varExpr = anfExprMap.get(anfVar);
                            exprList = getSubExprs(anfVar.getName(), varExpr, exprList);
                            break;
                        }
                    }
                }

                if (right instanceof ANFVar) {
                    ANFVar anfVar = (ANFVar) right;

                    for (Expr varName : anfExprMap.keySet()) {
                        ANFVar keyName = (ANFVar) varName;

                        if (keyName.getName().equals(anfVar.getName())) {
                            Expr varExpr = anfExprMap.get(anfVar);
                            exprList = getSubExprs(anfVar.getName(), varExpr, exprList);
                            break;
                        }
                    }
                }

                break;
            case "Call":
                Call call = (Call) subExpression;
                Expr funcExpr = call.getFunction();
                List<Expr> callArgs = call.getArguments();

                if (funcExpr instanceof ANFVar) {
                    ANFVar anfVar = (ANFVar) funcExpr;

                    for (Expr varName : anfExprMap.keySet()) {
                        ANFVar keyName = (ANFVar) varName;

                        if (keyName.getName().equals(anfVar.getName())) {
                            Expr varExpr = anfExprMap.get(anfVar);
                            exprList = getSubExprs(anfVar.getName(), varExpr, exprList);
                            break;
                        }
                    }
                }

                for (Expr arg : callArgs) {

                    if (arg instanceof ANFVar) {
                        ANFVar anfVar = (ANFVar) arg;

                        for (Expr varName : anfExprMap.keySet()) {
                            ANFVar keyName = (ANFVar) varName;

                            if (keyName.getName().equals(anfVar.getName())) {
                                Expr varExpr = anfExprMap.get(anfVar);
                                exprList = getSubExprs(anfVar.getName(), varExpr, exprList);
                                break;
                            }
                        }
                    }
                }

                break;
            case "Cons":
                Cons cons = (Cons) subExpression;
                List<Expr> consArgs = cons.getArguments();

                for (Expr arg : consArgs) {

                    if (arg instanceof ANFVar) {
                        ANFVar anfVar = (ANFVar) arg;

                        for (Expr varName : anfExprMap.keySet()) {
                            ANFVar keyName = (ANFVar) varName;

                            if (keyName.getName().equals(anfVar.getName())) {
                                Expr varExpr = anfExprMap.get(anfVar);
                                exprList = getSubExprs(anfVar.getName(), varExpr, exprList);
                                break;
                            }
                        }
                    }
                }

                break;
            default:
                break;
        }
        
        return exprList;
    }

    //recursive function for getting the sub-expression string
    public String getSubExprStr(Expr body) {
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
                String left = getSubExprStr(binOp.getLeft());
                String right = getSubExprStr(binOp.getRight());
                String op = binOp.getOperationStr();

                return funcBody += left + " " + op + " " + right + ";";
            case "If":
                If ifcon = (If) body;
                String ifCondStr = getSubExprStr(ifcon.getCondition());
                String ifThenStr = getSubExprStr(ifcon.getThenExpr());
                String ifElseStr = getSubExprStr(ifcon.getElseExpr());

                funcBody += "   if (" + ifCondStr + ") {";
                funcBody += "\n      " + ifThenStr;
                funcBody += "\n" + "   } else {";
                funcBody += "\n      " + ifElseStr;
                funcBody += "\n" + "   }";

                return funcBody;
            case "Call":
                Call call = (Call) body;
                Var callFunc = (Var) call.getFunction();
                String callFuncName = callFunc.getName();
                List<String> callArgStr = new ArrayList<>();

                for (Expr arg : call.getArguments()) {
                    callArgStr.add(getSubExprStr(arg));
                }
                
                String callArgList = "";

                if (callArgStr.size() != 0) {
                    if (callArgStr.size() == 1) {
                        callArgList = callArgStr.get(0);
                    } else {
                        callArgList = callArgStr.get(0);

                        for (int count = 1; count < callArgStr.size(); count++) {
                            callArgList += ", " + callArgStr.get(count);
                        }
                    }
                }

                return funcBody += callFuncName + "(" + callArgList + ");";  
            case "Case":
                Case casecon = (Case) body;
                String caseExprStr = getSubExprStr(casecon.getExpression());
                List<CaseAlt> caseAlts = casecon.getAlternatives();
                String caseAltsStr = "";

                for (CaseAlt casealt : caseAlts) {
                    caseAltsStr += getSubExprStr(casealt);
                }

                funcBody += "   switch (" + caseExprStr + ") {";
                funcBody += caseAltsStr;
                funcBody += "\n" + "   }";

                return funcBody; 
            case "CaseAlt":
                CaseAlt casealt = (CaseAlt) body;
                String casealtName = casealt.getName();
                //List<String> binders = casealt.getBinders();
                String casealtExprStr = getSubExprStr(casealt.getExpression());

                funcBody += "\n" + "      case " + "\"" + casealtName + "\"" + ":";
                funcBody += "\n" + "         " + casealtExprStr;
                funcBody += "\n" + "         " + "break";

                return funcBody;
            case "Cons":
                Cons cons = (Cons) body;
                String consName = cons.getName();
                List<Expr> consArgs = cons.getArguments();
                List<String> consArgStr = new ArrayList<>();

                for (Expr arg : consArgs) {
                    consArgStr.add(getSubExprStr(arg));
                }

                String consParamList = "";

                if (consArgStr.size() != 0) {
                    if (consArgStr.size() == 1) {
                        consParamList = consArgStr.get(0).toString();
                    } else {
                        consParamList = consArgStr.get(0).toString();

                        for (int count = 1; count < consArgStr.size(); count++) {
                            consParamList += ", " + consArgStr.get(count);
                        }
                    }
                }
                                                                                                
                funcBody += "new " + consName + "(" + consParamList + ")" + ";";

                return funcBody;
            case "Let":
                Let let = (Let) body;
                String letName = let.getName();
                String letBoundExprStr = getSubExprStr(let.getBoundExpr());
                String letBodyStr = getSubExprStr(let.getBody());

                funcBody += "   Object " + letName + " = " + letBoundExprStr;
                funcBody += "\n" + letBodyStr;

                return funcBody;
            default:
                break;
        }
        
        return funcBody;
    }
}