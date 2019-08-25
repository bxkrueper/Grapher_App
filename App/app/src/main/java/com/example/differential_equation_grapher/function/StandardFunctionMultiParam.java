package com.example.differential_equation_grapher.function;
//input: two variables, output: real number
public class StandardFunctionMultiParam {

    private Function function;
    private Variable[] variables;

    public StandardFunctionMultiParam(Function function, Variable... variables) {
        this.function = function;
        this.variables = variables;
    }
    public StandardFunctionMultiParam(String functionString, Variable... variables) {
        this(new BasicFunction(functionString),variables);
    }
    public StandardFunctionMultiParam(Function function, char... chars) {
        variables = new Variable[chars.length];
        for(int i=0;i<variables.length;i++){
            variables[i] = new Variable((chars[i]));
        }
        this.function = function;
    }
    public StandardFunctionMultiParam(String functionString, char... chars) {
        this(new BasicFunction(functionString),chars);
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public double evaluate(double... inputs) {
        if(variables.length!=inputs.length){
            return Double.NaN;
        }
        for(int i=0;i<inputs.length;i++){
            function.setVariable(variables[i],inputs[i]);
        }

        Constant answer = function.evaluate().convertToInstanceOf(RealNumber.additiveIdentity);
        if(answer instanceof RealNumber){
            return ((RealNumber) answer).getReal();
        }else{
            return Double.NaN;
        }
    }

    public StandardFunctionMultiParam getDerivative(Variable variable){
        return new StandardFunctionMultiParam(function.getDerivative(variable),variables);
    }
    public StandardFunctionMultiParam getDerivative(char charVar){
        return getDerivative(new Variable(charVar));
    }

    @Override
    public String toString() {
        return function.toDisplayString();
    }
}
