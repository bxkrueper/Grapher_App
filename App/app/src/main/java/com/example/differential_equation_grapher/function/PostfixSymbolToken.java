package com.example.differential_equation_grapher.function;

import java.util.Stack;

public abstract class PostfixSymbolToken implements Token{
    
    public void handleAssembleStacks(Stack<Expression> unitStack,Stack<BinarySymbolToken> binaryOperatorStack,Stack<PrefixSymbolToken> prefixOperatorStack){
        if(unitStack.isEmpty()){
          throw new RuntimeException("unit stack is empty! Can't apply postfix symbol");
      }
      //this ignores order of operations or has highest priority
      Expression ex = makeExpression(unitStack.pop());
      unitStack.push(ex);
    }
    
    public abstract PostfixExpression makeExpression(Expression ex);

}
