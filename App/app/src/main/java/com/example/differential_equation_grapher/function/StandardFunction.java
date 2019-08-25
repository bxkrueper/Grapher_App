package com.example.differential_equation_grapher.function;
//input: a single variable, output: real number
public class StandardFunction {

    private Function function;
    private Variable variable;

    public StandardFunction(Function function,Variable var) {
        this.function = function;
        this.variable = var;
    }
    public StandardFunction(String functionString,Variable var) {
        this(new BasicFunction(functionString),var);
    }
    public StandardFunction(Function function,char var) {
        this(function,new Variable(var));
    }
    public StandardFunction(String functionString,char var) {
        this(new BasicFunction(functionString),new Variable(var));
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public double evaluate(double input) {
        function.setVariable(variable,input);
        Constant answer = function.evaluate().convertToInstanceOf(RealNumber.additiveIdentity);
        if(answer instanceof RealNumber){
            return ((RealNumber) answer).getReal();
        }else{
            return (Double.NaN);
        }
    }

    public StandardFunction getDerivative(){
        return new StandardFunction(function.getDerivative(variable),variable);
    }


    //left must be lower than right, sampleCount must be >0
    //rectangles are centered on sample points
    public double integrate(double left, double right, int sampleCount) {
        if(sampleCount<=0){
            return 0;
        }
        boolean reversed = false;
        if(left>right){
            reversed = true;
            //swap values
            double temp = left;
            left = right;
            right = temp;
        }

        double dx = (right-left)/sampleCount;//name in the code assumes variable is x, but variable can by any letter
        double sum = 0.0;
        for(double x = left+dx/2;x<right;x+=dx){
            sum+=evaluate(x);//*dx is done after for loop for efficiency
        }
        sum*=dx;
        return reversed?sum*-1:sum;
    }

    @Override
    public String toString() {
        return function.toDisplayString();
    }
}
