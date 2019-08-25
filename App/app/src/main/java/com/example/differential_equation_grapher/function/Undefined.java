package com.example.differential_equation_grapher.function;

import java.util.Collection;
import java.util.Set;

public class Undefined extends Constant{
    
    private static Undefined instance;
    
    private Undefined(){
        
    }
    
    public static Undefined getInstance(){
        if(instance==null){
            instance = new Undefined();
        }
        
        return instance;
    }

    @Override
    public String toDisplayString() {
        return "Undefined";
    }

    @Override
    public Expression copy() {
        return this;//immutable
    }

    @Override
    public Constant getAdditiveIdentity() {
        return this;
    }

    @Override
    public Constant getMultiplictiveIdentity() {
        return this;
    }

    @Override
    public Constant getNaN() {
        return this;
    }

    @Override
    public Expression getDerivative(Variable v) {
        return this;
    }
    
    public int getNumberOfChildren(){
        return 0;
    }
    public Expression getChild(int position){
        return null;
    }

    @Override
    public Expression simplify() {
        return this;
    }

    

}
