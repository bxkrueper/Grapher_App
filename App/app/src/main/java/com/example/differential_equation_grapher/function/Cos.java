package com.example.differential_equation_grapher.function;

public class Cos extends TrigFunction{
    
    public static final String NAME = "cos";
    
    public Cos(Expression expression) {
        super(new ArgumentSingle(expression));
    }
    public Cos(Arguments arguments) {
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
    public Expression getDerivative(Variable v) {
        Expression inside = getFirstArg();
        return new Product(inside.getDerivative(v),new Negate(new Sin(inside.copy())));
    }
    
    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Cos(newArguments);
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }
    
    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.cos(getFirstArg().evaluate(substitution));
    }
    
    
}
