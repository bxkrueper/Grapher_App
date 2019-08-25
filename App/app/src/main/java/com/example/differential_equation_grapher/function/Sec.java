package com.example.differential_equation_grapher.function;

public class Sec extends TrigFunction{

    public static final String NAME = "sec";

    public Sec(Expression expression) {
        super(new ArgumentSingle(expression));
    }
    public Sec(Arguments arguments) {
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
        return new Product(inside.getDerivative(v),new Sec(inside.copy()),new Tan(inside.copy()));
    }

    @Override
    public Expression getSubclassInstance(Arguments newArguments) {
        return new Sec(newArguments);
    }
    @Override
    public boolean argumentsAreCompatable(Arguments arguments) {
        return arguments.getNumberOfArguments()==1;
    }

    @Override
    public Constant evaluate(Substitution substitution) {
        return ConstantMediator.divide(RealNumber.multiplictiveIdentity,ConstantMediator.cos(getFirstArg().evaluate(substitution)));
    }


}
