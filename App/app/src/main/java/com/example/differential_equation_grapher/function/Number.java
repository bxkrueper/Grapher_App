package com.example.differential_equation_grapher.function;

//all numbers: complex or real
public abstract class Number extends Constant{
    

    
    public abstract double getReal();
    
    public abstract double getImaginary();
    
    public abstract double getMagnitude();
    
    public abstract double getTheta();
    
    @Override
    public Expression getDerivative(Variable v) {
        return new RealNumber(0.0);
    }
}
