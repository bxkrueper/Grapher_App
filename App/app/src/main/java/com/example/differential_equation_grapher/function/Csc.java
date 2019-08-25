package com.example.differential_equation_grapher.function;

public class Csc extends TrigFunction{

    public static final String NAME = "csc";

    public Csc(Expression expression) {
        super(new ArgumentSingle(expression));
    }
    public Csc(Arguments arguments) {
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
        return new Product(inside.getDerivative(v),new Negate(new Csc(inside.copy())),new Cot(inside.copy()));
    }

    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Csc(newArguments);
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.divide(RealNumber.multiplictiveIdentity,ConstantMediator.sin(getFirstArg().evaluate(substitution)));
    }

}
