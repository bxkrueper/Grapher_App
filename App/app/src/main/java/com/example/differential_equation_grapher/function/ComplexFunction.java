package com.example.differential_equation_grapher.function;

public class ComplexFunction {
    private Function function;
    private Variable variable;

    private static final Complex exampleComplexNumber = new ComplexRI(Double.NaN,Double.NaN);//used only as a Complex class for ConvertToInstanceOf

    public ComplexFunction(Function function) {
        this.function = function;
        this.variable = new Variable('z');
    }
    public ComplexFunction(String functionString) {
        this(new BasicFunction(functionString));
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public ComplexFunction getDerivative(){
        return new ComplexFunction(function.getDerivative(variable));
    }

    public Complex evaluate(double real,double imaginary) {
        return evaluate(new ComplexRI(real,imaginary));/////////////making new object every time
    }

    public Complex evaluate(Complex input) {
        function.setVariable(variable,input);
        Constant answer = function.evaluate().convertToInstanceOf(exampleComplexNumber);
        if(answer instanceof Complex){
            return ((Complex) answer);
        }else if(answer instanceof RealNumber){
            return (new ComplexRI(((RealNumber) answer).getReal(),0));
        }else{
            return ComplexRI.nan;
        }
    }

//    public StandardFunction getDerivative(){
//        return new StandardFunction(function.getDerivative(variable));
//    }

    @Override
    public String toString() {
        return function.toDisplayString();
    }


}
