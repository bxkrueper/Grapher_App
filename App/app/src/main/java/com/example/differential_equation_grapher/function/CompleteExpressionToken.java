package com.example.differential_equation_grapher.function;

import java.util.Stack;

//this token contains a complete expression, not just a symbol
public class CompleteExpressionToken implements Token{

    private Expression expression;
    
    public CompleteExpressionToken(Expression e) {
        if(e==null){
            throw new RuntimeException("CompleteExpressionToken should not accept a null parameter");
        }
        
        this.expression = e;
    }

    @Override
    public void handleAssembleStacks(Stack<Expression> unitStack,Stack<BinarySymbolToken> binaryOperatorStack,Stack<PrefixSymbolToken> prefixOperatorStack) {
        while(!prefixOperatorStack.isEmpty()){
            expression = prefixOperatorStack.pop().makeExpression(expression);
        }
        unitStack.push(expression);
    }

}
