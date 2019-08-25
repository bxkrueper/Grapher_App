package com.example.differential_equation_grapher.function;

import java.util.Stack;

public abstract class BinarySymbolToken implements Token{
    
    public abstract int getPriority();
    public abstract Expression makeExpression(Expression ex1,Expression ex2);

    @Override
    public void handleAssembleStacks(Stack<Expression> unitStack,Stack<BinarySymbolToken> binaryOperatorStack,Stack<PrefixSymbolToken> prefixOperatorStack) {
        if(!prefixOperatorStack.isEmpty()){
            throw new RuntimeException("can't apply unary operator to binary operator!");
        }
        
        boolean flag = true;
        while(flag){
            //if this op is greater than the symbol at the top of the stack (or if there is nothing in the stack)
            if(binaryOperatorStack.isEmpty() || this.getPriority()>binaryOperatorStack.peek().getPriority()){
                binaryOperatorStack.push(this);
                flag = false;
            }   
            else{//if this op is lower than or the same as the symbol at the top of the stack
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
        }
        
    }
    

}
