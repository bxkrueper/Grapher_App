package com.example.differential_equation_grapher.function;

public class Sinh extends TrigFunction{

    public static final String NAME = "sinh";

    public Sinh(Expression expression) {
        super(new ArgumentSingle(expression));
    }
    public Sinh(Arguments arguments) {
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
    
    
    
    
    //d(inside) * cosh(inside)
    @Override
    public Expression getDerivative(Variable v) {
        Expression inside = getFirstArg();
        return new Product(inside.getDerivative(v),new Cosh(inside.copy()));
    }
    
    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Sinh(newArguments);
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }
    
    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.sinh(getFirstArg().evaluate(substitution));
    }
    
}
