package com.example.differential_equation_grapher.function;

public class Tan extends TrigFunction{
    
    public static final String NAME = "tan";
    
    public Tan(Expression expression) {
        super(new ArgumentSingle(expression));
    }
    public Tan(Arguments arguments) {
        super(arguments);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Expression specialSimplification() {
        // TODO Auto-generated method stub
        return this;
    }
    
    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Tan(newArguments);
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }
    
    ///////change to sec^2?
    //dx*(1+tan(x)^2)
    @Override
    public Expression getDerivative(Variable v) {
        return new Product(getFirstArg().getDerivative(v),new Sum(new RealNumber(1),new Power(this.copy(),new RealNumber(2))));
    }
    
    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.tan(getFirstArg().evaluate(substitution));
    }
    
}
