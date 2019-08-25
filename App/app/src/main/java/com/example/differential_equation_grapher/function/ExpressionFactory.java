/*
 * converts a string to an expression object
 */

package com.example.differential_equation_grapher.function;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

public class ExpressionFactory {
    
    private static List<Tokenizer> parseOrder;

    //returns undefined if there is a problem making the expression
    public static Expression getExpression(String str){
        if(parseOrder==null){
            makeParseOrder();
        }

        Expression ex;
        try{
            List<Token> tokenList = tokenizeExpression(str);//converts a string to a list of tokens
            clarifySymbolList(tokenList);//edits token list to make it more compatible for assembly
            ex = assemble(tokenList);
//        ex = ex.simplify();

        }catch(Exception e){
            ex = Undefined.getInstance();
        }
        return ex;
    }

    //converts a string into a double ( "1+1" will return 2 and PI will return 3.14....)
    //returns Double.NaN if it has a problem parsing the expression or it has variables in it
    public static double getRealNumber(String string){
        Expression expression = getExpression(string);
        Constant constant = expression.evaluate(NullSubstitution.getInstance()).convertToInstanceOf(RealNumber.additiveIdentity);;
        if(constant instanceof RealNumber){
            return ((RealNumber) constant).getReal();
        }else{
            return Double.NaN;
        }
    }

    public static Complex getComplexNumber(String string){
        Expression expression = getExpression(string);
        Constant constant = expression.evaluate(NullSubstitution.getInstance()).convertToInstanceOf(ComplexRI.additiveIdentity);;
        if(constant instanceof Complex){
            return ((Complex) constant);
        }else{
            return ComplexRI.nan;
        }
    }
    
    //uses the reference order list to guess at the next token from the string and add it to the list
    private static List<Token> tokenizeExpression(String exp){
        List<Token> tokenList = new LinkedList<>();
        StringParser sp = new StringParser(exp);
        
        while(sp.hasNext()){
            Token token = null;
            Iterator<Tokenizer> it = parseOrder.iterator();
            while(token==null && it.hasNext()){
                token=it.next().tryTokenize(sp);
            }
            if(token==null){//no tokens could be pulled from the next part of the string
                throw new RuntimeException("couldn't parse expression: no tokens could be pulled from the next part of the string: " + sp);
            }else{
                tokenList.add(token);
            }
        }
        return tokenList;
    }

    private static void clarifySymbolList(List<Token> tokenedExpressionList) {
        handleSubtractNegate(tokenedExpressionList);
        handleInvisibleMultiplication(tokenedExpressionList);
    }

    //uses stacks to assemble expressions, binary oparators, and unary operators in the right order
    //unary operators are given priority
    /////////need to fix: -x^2 gives (-x)^2  should be -(x^2)
    private static Expression assemble(List<Token> tokenList) {
        Stack<Expression> unitStack = new Stack<>();
        Stack<BinarySymbolToken> binaryOperatorStack = new Stack<>();
        Stack<PrefixSymbolToken> prefixOperatorStack = new Stack<>();
        
        //main part
        for(Token t:tokenList){
            t.handleAssembleStacks(unitStack,binaryOperatorStack,prefixOperatorStack);
//            System.out.println("unitStack: " + unitStack + "opStack: " + operatorStack);
        }
        //next: finish up 
        
        //there shouldn't be any prefixes left
        if(!prefixOperatorStack.isEmpty()){
            throw new RuntimeException("dangling prefix operator");
        }
        
        //use up the rest of the binary tokens
        while(!binaryOperatorStack.isEmpty()){
            Expression ex2 = unitStack.pop();
            Expression ex1;
            if(unitStack.isEmpty()){
                throw new RuntimeException("unit stack is empty: not enough expressions for binary operator");
            }else{
                ex1 = unitStack.pop();
            }
            
            Expression bex = binaryOperatorStack.pop().makeExpression(ex1,ex2);
            unitStack.push(bex);
        }
        
        //there should be one expression left in the stack: the answer
        if(unitStack.isEmpty()){
            throw new RuntimeException("unit stack is empty!");
        }else{
            Expression ex = unitStack.pop();
            if(!unitStack.isEmpty()){
                throw new RuntimeException("unit stack is not empty at end of assembly! _" + unitStack.pop().toDisplayString() + "_ ");
            }
            return ex;
        }
        
    }
    
    //inserts a sum token before every negate token if it if after a complete expression
    //other wise turns the negate into a (-1)*
    private static void handleSubtractNegate(List<Token> tokenedExpressionList){
        ListIterator<Token> it = tokenedExpressionList.listIterator();
        Token prev = null;
        while(it.hasNext()){
            Token current = it.next();
            if(current instanceof TokenizerFactory.NegateTokenizer.NegateToken){
                if(prev instanceof CompleteExpressionToken){
                    //insert a sum token
                    it.previous();
                    it.add(new TokenizerFactory.SumTokenizer.SumToken());
                    it.next();
                }
                if(prev instanceof TokenizerFactory.PowerTokenizer.PowerToken){
                    //do nothing. prioritize the negate over the power so x^-2 will not be x^-1 * 2
                }else{
                    //replace the negate with a (-1) token and a * token   (this makes sure the negate doesn't override order of operations so -x^2 will be -(x^2)
                    it.remove();
                    it.add(new CompleteExpressionToken(new RealNumber(-1)));/////add the same -1 every time?
                    it.add(new TokenizerFactory.ProductTokenizer.ProductToken());
                    //don't call it.next. next ignore the ones that were just added
                }


            }
            prev=current;
        }
    }
    
    //inserts a multiplication token between consecutive expression tokens
    private static void handleInvisibleMultiplication(List<Token> tokenedExpressionList){
        ListIterator<Token> it = tokenedExpressionList.listIterator();
        Token prev = null;
        while(it.hasNext()){
            Token current = it.next();
            if((current instanceof CompleteExpressionToken) && (prev instanceof CompleteExpressionToken)){
                it.previous();
                it.add(new TokenizerFactory.ProductTokenizer.ProductToken());
                it.next();
            }
            prev=current;
        }
    }

    //constructs the list of tokenizers in the order they should be used to tokenize the expression string
    //order matters here. (like parsing whole functions names first instead of parsing a single letter off the function name and making it a variable
    private static void makeParseOrder(){
        parseOrder = new ArrayList<>();
        parseOrder.add(new TokenizerFactory.WhiteSpaceIgnorerTokenizer());
        parseOrder.add(new TokenizerFactory.ParenthesesTokenizer());
        parseOrder.add(new TokenizerFactory.VectorTokenizer());
        parseOrder.add(new TokenizerFactory.ExpressionGroupTokenizer());
        parseOrder.add(new TokenizerFactory.RealNumberTokenizer());
        parseOrder.add(new TokenizerFactory.AbsoluteValueTokenizer());
        parseOrder.add(new TokenizerFactory.SqrtTokenizer());
        parseOrder.add(new TokenizerFactory.ITokenizer());
        parseOrder.add(new TokenizerFactory.ETokenizer());
        parseOrder.add(new TokenizerFactory.PITokenizer());
        parseOrder.add(new TokenizerFactory.MaxTokenizer());
        parseOrder.add(new TokenizerFactory.MinTokenizer());
        parseOrder.add(new TokenizerFactory.CeilTokenizer());
        parseOrder.add(new TokenizerFactory.FloorTokenizer());
        parseOrder.add(new TokenizerFactory.DerivativeTokenizer());
        parseOrder.add(new TokenizerFactory.SinhTokenizer());//put before sin
        parseOrder.add(new TokenizerFactory.CoshTokenizer());//put before cos
        parseOrder.add(new TokenizerFactory.TanhTokenizer());//put before tan
        parseOrder.add(new TokenizerFactory.SinTokenizer());
        parseOrder.add(new TokenizerFactory.CosTokenizer());
        parseOrder.add(new TokenizerFactory.TanTokenizer());
        parseOrder.add(new TokenizerFactory.CscTokenizer());
        parseOrder.add(new TokenizerFactory.SecTokenizer());
        parseOrder.add(new TokenizerFactory.CotTokenizer());
        parseOrder.add(new TokenizerFactory.ArcsinTokenizer());
        parseOrder.add(new TokenizerFactory.ArccosTokenizer());
        parseOrder.add(new TokenizerFactory.ArctanTokenizer());
        parseOrder.add(new TokenizerFactory.LogTokenizer());
        parseOrder.add(new TokenizerFactory.LnTokenizer());
        parseOrder.add(new TokenizerFactory.VariableTokenizer());//put after all other things that have letters in them
        parseOrder.add(new TokenizerFactory.SumTokenizer());
        parseOrder.add(new TokenizerFactory.NegateTokenizer());////negates will be turned into subtracts or *-1 where appropriate later
        parseOrder.add(new TokenizerFactory.ProductTokenizer());
        parseOrder.add(new TokenizerFactory.DivideTokenizer());
        parseOrder.add(new TokenizerFactory.PowerTokenizer());
        parseOrder.add(new TokenizerFactory.FactorialTokenizer());
        parseOrder.add(new TokenizerFactory.DegreesTokenizer());
        parseOrder.add(new TokenizerFactory.AbsoluteValueBarsTokenizer());
    }
    

}