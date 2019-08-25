package com.example.differential_equation_grapher.function;

public class Cosh extends TrigFunction{

    public static final String NAME = "cosh";

    public Cosh(Expression expression) {
        super(new ArgumentSingle(expression));
    }
    public Cosh(Arguments arguments) {
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




    //d(inside) * sinh(inside)
    @Override
    public Expression getDerivative(Variable v) {
        Expression inside = getFirstArg();
        return new Product(inside.getDerivative(v),new Sinh(inside.copy()));
    }
    
    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Cosh(newArguments);
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }
    
    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.cosh(getFirstArg().evaluate(substitution));
    }
    
}
