package com.example.differential_equation_grapher.function;

public abstract class PostfixExpression extends UnaryExpression{

    public PostfixExpression(Arguments arguments) {
        super(arguments);
    }
    
    
    @Override
    public String toDisplayString() {
        if(getFirstArg() instanceof ChainOperation){
            return "(" + getFirstArg().toDisplayString() + ")" + getSymbol();
        }else{
            return getFirstArg().toDisplayString() + getSymbol();
        }
        
    }

}
