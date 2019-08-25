package com.example.differential_equation_grapher.function;
//input: theta, output: radius
public class PolarFunction {

    private Function function;
    private Variable variable;

    public PolarFunction(Function function) {
        this.function = function;
        this.variable = new Variable('\u03B8');
    }
    public PolarFunction(String functionString) {
        this(new BasicFunction(functionString));
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    //returns radius given theta
    public double evaluate(double t) {
        function.setVariable(variable,t);
        Constant answer = function.evaluate().convertToInstanceOf(RealNumber.additiveIdentity);
        if(answer instanceof RealNumber){
            return ((RealNumber) answer).getReal();
        }else{
            return (Double.NaN);
        }
    }

    public double evaluateX(double theta){
        return polarToX(evaluate(theta),theta);
    }

    public double evaluateY(double theta){
        return polarToY(evaluate(theta),theta);
    }

    public double polarToX(double radius,double theta){
        return radius*Math.cos(theta);
    }

    public double polarToY(double radius,double theta){
        return radius*Math.sin(theta);
    }

    @Override
    public String toString() {
        return function.toDisplayString();
    }
}
