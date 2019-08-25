package com.example.differential_equation_grapher.function;

import java.util.List;

public abstract class UnaryExpression extends Operation{
    
    public UnaryExpression(Arguments arguments) {
        super(arguments);
    }
    
    @Override
    public boolean isEqualTo(Object o) {
        if(o instanceof UnaryExpression){
            UnaryExpression u2 = (UnaryExpression) o;
            if(!this.getSymbol().equals(u2.getSymbol())){
                return false;
            }
            return this.getFirstArg().equals(u2.getFirstArg());
        }else{
            return false;
        }
    }

    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }
    
    public abstract String getSymbol();
    
    
}
