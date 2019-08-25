package com.example.differential_equation_grapher.function;

import java.util.Stack;

//these classes represent a recognized piece of an expression string and helps make the expression tree
public interface Token {
    public void handleAssembleStacks(Stack<Expression> unitStack,Stack<BinarySymbolToken> binaryOperatorStack,Stack<PrefixSymbolToken> prefixOperatorStack);
}
