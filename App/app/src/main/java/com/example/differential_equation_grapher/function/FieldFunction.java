package com.example.differential_equation_grapher.function;

//input: x,y real number output: x or y magnitude of arrow (real number)
public class FieldFunction {

    private Function xComponentFunction;
    private Function yComponentFunction;
    private Variable variableX;
    private Variable variableY;

    public FieldFunction(Function xComponentFunction, Function yComponentFunction) {
        this.xComponentFunction = xComponentFunction;
        this.yComponentFunction = yComponentFunction;
        this.variableX = new Variable('x');
        this.variableY = new Variable('y');
    }
    public FieldFunction(String xComponentFunctionString, String yComponentFunctionString){
        this(new BasicFunction(xComponentFunctionString),new BasicFunction(yComponentFunctionString));
    }

    public Function getxComponentFunction() {
        return xComponentFunction;
    }

    public void setxComponentFunction(Function xComponentFunction) {
        this.xComponentFunction = xComponentFunction;
    }

    public Function getyComponentFunction() {
        return yComponentFunction;
    }

    public void setyComponentFunction(Function yComponentFunction) {
        this.yComponentFunction = yComponentFunction;
    }

    public double evaluateXComponent(double x,double y) {
        xComponentFunction.setVariable('x',x);
        xComponentFunction.setVariable('y',y);
        Constant answer = xComponentFunction.evaluate().convertToInstanceOf(RealNumber.additiveIdentity);
        if(answer instanceof RealNumber){
            return ((RealNumber) answer).getReal();
        }else{
            return (Double.NaN);
        }
    }

    public double evaluateYComponent(double x,double y) {
        yComponentFunction.setVariable('x',x);
        yComponentFunction.setVariable('y',y);
        Constant answer = yComponentFunction.evaluate().convertToInstanceOf(RealNumber.additiveIdentity);
        if(answer instanceof RealNumber){
            return ((RealNumber) answer).getReal();
        }else{
            return (Double.NaN);
        }
    }
}
