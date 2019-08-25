package com.example.differential_equation_grapher.function;

public abstract class ChainOperation extends Operation{
    public ChainOperation(Arguments arguments) {
        super(arguments);
    }
    
    public abstract String getSymbol();

    public abstract int getPriority();
}
