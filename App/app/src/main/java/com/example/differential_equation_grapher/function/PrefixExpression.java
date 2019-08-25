package com.example.differential_equation_grapher.function;

public abstract class PrefixExpression extends UnaryExpression{

    public PrefixExpression(Arguments arguments) {
        super(arguments);
    }

    @Override
    public String toDisplayString() {
        if(getFirstArg() instanceof ChainOperation){
            return getSymbol() + "(" + getFirstArg().toDisplayString() + ")";
        }else{
            return getSymbol() + getFirstArg().toDisplayString();
        }
        
    }

}
