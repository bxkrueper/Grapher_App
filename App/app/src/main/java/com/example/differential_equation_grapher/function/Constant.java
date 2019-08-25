package com.example.differential_equation_grapher.function;

import java.util.List;

//constants are immutable
public abstract class Constant extends Expression implements Comparable<Constant>{
    
    @Override
    public final boolean computable(){
        return true;
    }
    
    @Override
    public final Expression substitute(Substitution substitution){
        return this;
    }
    
    @Override
    public final Constant evaluate(Substitution substitution){
        return this;
    }
    
    @Override
    public Expression copy() {
        return this;//Constant is immutable
    }
    
    @Override
    public final boolean isEqualTo(Object o){
        if(o instanceof Constant){
            return ConstantMediator.equals(this, (Constant) o);
        }else{
            return false;
        }
    }
    
    @Override
    public String toDebugString(){
        return toDisplayString();
    }
    
    public Constant add(Constant c2){
        return ConstantMediator.add(this,c2);
    }
    public Constant subtract(Constant c2){
        return ConstantMediator.subtract(this,c2);
    }
    public Constant multiply(Constant c2){
        return ConstantMediator.multiply(this,c2);
    }
    public Constant divide(Constant c2){
        return ConstantMediator.divide(this,c2);
    }
    public Constant power(Constant c2){
        return ConstantMediator.power(this,c2);
    }
    public Constant convertToInstanceOf(Constant c2){
        return ConstantMediator.convertToInstanceOf(this,c2);
    }
    public Constant abs(){
        return ConstantMediator.abs(this);
    }
    @Override
    public int compareTo(Constant c2){
        return ConstantMediator.compareTo(this,c2);
    }
    public Constant gamma(){
        return ConstantMediator.gamma(this);
    }
    public Constant negate(){
        return ConstantMediator.negate(this);
    }
    public Constant round(double roundTo){
        return ConstantMediator.round(this,roundTo);
    }
    
    public abstract Constant getAdditiveIdentity();//what makes no difference when adding and subtracting/multiply to get zero
    public abstract Constant getMultiplictiveIdentity();//what makes no difference when multiplying
    public abstract Constant getNaN();//contains Double.NaN for parameters
    
    
}
